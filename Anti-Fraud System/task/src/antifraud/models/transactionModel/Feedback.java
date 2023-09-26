package antifraud.models.transactionModel;

/**
 * Enum representing different feedback values for a transaction in the anti-fraud system.
 * Feedback can be one of the following: ALLOWED, MANUAL_PROCESSING, or PROHIBITED.
 */
public enum Feedback {
    /**
     * Feedback indicating that the transaction is allowed.
     */
    ALLOWED,

    /**
     * Feedback indicating that the transaction requires manual processing.
     */
    MANUAL_PROCESSING,

    /**
     * Feedback indicating that the transaction is prohibited.
     */
    PROHIBITED
}
