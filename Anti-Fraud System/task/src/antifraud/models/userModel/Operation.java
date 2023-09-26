package antifraud.models.userModel;

/**
 * An enumeration representing operations that can be performed on a user's access permissions in the anti-fraud system.
 */
public enum Operation {
    /**
     * The LOCK operation is used to restrict a user's access.
     */
    LOCK,

    /**
     * The UNLOCK operation is used to grant or restore a user's access.
     */
    UNLOCK
}
