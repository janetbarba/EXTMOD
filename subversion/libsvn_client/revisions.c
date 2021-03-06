/*
 * revisions.c:  discovering revisions
 *
 * ====================================================================
 * Copyright (c) 2000-2002 CollabNet.  All rights reserved.
 *
 * This software is licensed as described in the file COPYING, which
 * you should have received as part of this distribution.  The terms
 * are also available at http://subversion.tigris.org/license-1.html.
 * If newer versions of this license are posted there, you may use a
 * newer version instead, at your option.
 *
 * This software consists of voluntary contributions made by many
 * individuals.  For exact contribution history, see the revision
 * history and logs, available at http://subversion.tigris.org/.
 * ====================================================================
 */



#include <apr_pools.h>

#include "svn_error.h"
#include "svn_string.h"
#include "svn_ra.h"
#include "svn_wc.h"
#include "svn_client.h"
#include "svn_path.h"
#include "client.h"





svn_error_t *
svn_client__get_revision_number (svn_revnum_t *revnum,
                                 svn_ra_plugin_t *ra_lib,
                                 void *sess,
                                 const svn_client_revision_t *revision,
                                 const char *path,
                                 apr_pool_t *pool)
{
  /* ### When revision->kind == svn_client_revision_date, is there an
     optimization such that we can compare revision->value->date with
     the committed-date in the entries file (or rather, with some
     range of which committed-date is one endpoint), and sometimes
     avoid a trip over the RA layer?  The only optimizations I can
     think of involve examining other entries to build a timespan
     across which committed-revision is known to be the head, but it
     doesn't seem worth it.  -kff */

  /* Sanity check. */
  if (((ra_lib == NULL) || (sess == NULL))
      && ((revision->kind == svn_client_revision_date)
          || (revision->kind == svn_client_revision_head)))
    {
      return svn_error_create
        (SVN_ERR_CLIENT_RA_ACCESS_REQUIRED, 0, NULL, pool,
         "svn_client__get_revision_number: "
         "need ra_lib and session for date or head revisions.");
    }

  if (revision->kind == svn_client_revision_number)
    *revnum = revision->value.number;
  else if (revision->kind == svn_client_revision_date)
    SVN_ERR (ra_lib->get_dated_revision (sess, revnum, revision->value.date));
  else if (revision->kind == svn_client_revision_head)
    SVN_ERR (ra_lib->get_latest_revnum (sess, revnum));
  else if (revision->kind == svn_client_revision_unspecified)
    *revnum = SVN_INVALID_REVNUM;
  else if ((revision->kind == svn_client_revision_committed)
           || (revision->kind == svn_client_revision_working)
           || (revision->kind == svn_client_revision_base)
           || (revision->kind == svn_client_revision_previous))
    {
      /* Darn it, I am not going to propagate the stringbuf madness
         through to this function's interface. */
      svn_stringbuf_t *path_strbuf;
      svn_wc_entry_t *ent;

      /* Sanity check. */
      if (path == NULL)
        return svn_error_create
          (SVN_ERR_CLIENT_VERSIONED_PATH_REQUIRED, 0, NULL, pool,
           "svn_client__get_revision_number: "
           "need a version-controlled path to fetch local revision info.");

      path_strbuf = svn_stringbuf_create (path, pool);
      SVN_ERR (svn_wc_entry (&ent, path_strbuf, pool));

      if (! ent)
        return svn_error_createf
        (SVN_ERR_UNVERSIONED_RESOURCE, 0, NULL, pool,
         "svn_client__get_revision: '%s' not under revision control", path);
      
      if ((revision->kind == svn_client_revision_base)
          || (revision->kind == svn_client_revision_working))
        *revnum = ent->revision;
      else
        {
          *revnum = ent->cmt_rev;
          if (revision->kind == svn_client_revision_previous)
            (*revnum)--;
        }
    }
  else
    return svn_error_createf
      (SVN_ERR_CLIENT_BAD_REVISION, 0, NULL, pool,
       "svn_client__get_revision_number: "
       "unrecognized revision type requested for '%s'", path);
  
  return SVN_NO_ERROR;
}



/* 
 * local variables:
 * eval: (load-file "../../tools/dev/svn-dev.el")
 * end: */
