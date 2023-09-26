package antifraud.models.transactionModel;

/**
 * An enum representing different results for transactions in the anti-fraud system.
 */
public enum Result {
    ALLOWED,            // Transaction is allowed
    MANUAL_PROCESSING,  // Transaction requires manual processing
    PROHIBITED          // Transaction is prohibited
}
