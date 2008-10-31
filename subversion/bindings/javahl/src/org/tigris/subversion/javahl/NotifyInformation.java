/**
 * @copyright
 * ====================================================================
 * Copyright (c) 2003-2005,2007 CollabNet.  All rights reserved.
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
 * @endcopyright
 */

package org.tigris.subversion.javahl;

import java.util.EventObject;

/**
 * The event passed to the {@link Notify2#onNotify(NotifyInformation)}
 * API to notify {@link SVNClientInterface} of relevant events.
 *
 * @since 1.2
 */
public class NotifyInformation extends EventObject
{
    // Update the serialVersionUID when there is a incompatible change
    // made to this class.  See any of the following, depending upon
    // the Java release.
    // http://java.sun.com/j2se/1.3/docs/guide/serialization/spec/version.doc7.html
    // http://java.sun.com/j2se/1.4/pdf/serial-spec.pdf
    // http://java.sun.com/j2se/1.5.0/docs/guide/serialization/spec/version.html#6678
    // http://java.sun.com/javase/6/docs/platform/serialization/spec/version.html#6678
    private static final long serialVersionUID = 1L;

    /**
     * The {@link NotifyAction} which triggered this event.
     */
    private int action;

    /**
     * The {@link NodeKind} of the item.
     */
    private int kind;

    /**
     * The MIME type of the item.
     */
    private String mimeType;

    /**
     * Any lock for the item.
     */
    private Lock lock;

    /**
     * Any error message for the item.
     */
    private String errMsg;

    /**
     * The {@link NotifyStatus} of the content of the item.
     */
    private int contentState;

    /**
     * The {@link NotifyStatus} of the properties of the item.
     */
    private int propState;

    /**
     * The {@link LockStatus} of the lock of the item.
     */
    private int lockState;

    /**
     * The revision of the item.
     */
    private long revision;

    /**
     * The name of the changelist.
     * @since 1.5
     */
    private String changelistName;

    /**
     * The range of the merge just beginning to occur.
     * @since 1.5
     */
    private RevisionRange mergeRange;

    /**
     * A common absolute path prefix that can be subtracted from .path.
     * @since 1.6
     */
    private String pathPrefix;

    /**
     * Whether .path is the victim of a tree-conflict.
     * @since 1.6
     */
    private boolean treeConflicted;

    /**
     * This constructor is to be used by the native code.
     *
     * @param path The path of the item, which is the source of the event.
     * @param action The {@link NotifyAction} which triggered this event.
     * @param kind The {@link NodeKind} of the item.
     * @param mimeType The MIME type of the item.
     * @param lock Any lock for the item.
     * @param errMsg Any error message for the item.
     * @param contentState The {@link NotifyStatus} of the content of
     * the item.
     * @param propState The {@link NotifyStatus} of the properties of
     * the item.
     * @param lockState The {@link LockStatus} of the lock of the item.
     * @param revision The revision of the item.
     * @param changelistName The name of the changelist.
     * @param mergeRange The range of the merge just beginning to occur.
     * @param pathPrefix A common path prefix.
     * @param treeConflicted Whether this path is a victim of a tree conflict.
     */
    NotifyInformation(String path, int action, int kind, String mimeType,
                      Lock lock, String errMsg, int contentState,
                      int propState, int lockState, long revision,
                      String changelistName, RevisionRange mergeRange,
                      String pathPrefix, boolean treeConflicted)
    {
        super(path);
        this.action = action;
        this.kind = kind;
        this.mimeType = mimeType;
        this.lock = lock;
        this.errMsg = errMsg;
        this.contentState = contentState;
        this.propState = propState;
        this.lockState = lockState;
        this.revision = revision;
        this.changelistName = changelistName;
        this.mergeRange = mergeRange;
        this.pathPrefix = pathPrefix;
        this.treeConflicted = treeConflicted;
    }

    /**
     * @return The path of the item, which is the source of the event.
     */
    public String getPath()
    {
        return (String) super.source;
    }

    /**
     * @return The {@link NotifyAction} which triggered this event.
     */
    public int getAction()
    {
        return action;
    }

    /**
     * @return The {@link NodeKind} of the item.
     */
    public int getKind()
    {
        return kind;
    }

    /**
     * @return The MIME type of the item.
     */
    public String getMimeType()
    {
        return mimeType;
    }

    /**
     * @return Any lock for the item.
     */
    public Lock getLock()
    {
        return lock;
    }

    /**
     * @return Any error message for the item.
     */
    public String getErrMsg()
    {
        return errMsg;
    }

    /**
     * @return The {@link NotifyStatus} of the content of the item.
     */
    public int getContentState()
    {
        return contentState;
    }

    /**
     * @return The {@link NotifyStatus} of the properties of the item.
     */
    public int getPropState()
    {
        return propState;
    }

    /**
     * @return The {@link LockStatus} of the lock of the item.
     */
    public int getLockState()
    {
        return lockState;
    }

    /**
     * @return The revision of the item.
     */
    public long getRevision()
    {
        return revision;
    }

    /**
     * @return The name of the changelist.
     * @since 1.5
     */
    public String getChangelistName()
    {
        return changelistName;
    }

    /**
     * @return The range of the merge just beginning to occur.
     * @since 1.5
     */
    public RevisionRange getMergeRange()
    {
        return mergeRange;
    }

    /**
     * @return The common absolute path prefix.
     * @since 1.6
     */
    public String getPathPrefix()
    {
        return pathPrefix;
    }

    /**
     * @return Whether the .path is tree conflicted.
     * @since 1.6
     */
    public boolean getTreeConflicted()
    {
        return treeConflicted;
    }
}
