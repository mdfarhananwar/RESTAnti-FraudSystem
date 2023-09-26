package antifraud.models.transactionModel;

/**
 * A request model used for providing feedback for a transaction in the anti-fraud system.
 */
public class TransactionRequestFeedback {
    private Long transactionId;
    private Feedback feedback;

    /**
     * Default constructor for the TransactionRequestFeedback class.
     */
    public TransactionRequestFeedback() {
    }

    /**
     * Constructor for creating a TransactionRequestFeedback with specified properties.
     *
     * @param transactionId The ID of the transaction for which feedback is provided.
     * @param feedback The feedback provided for the transaction (ALLOWED, MANUAL_PROCESSING, or PROHIBITED).
     */
    public TransactionRequestFeedback(Long transactionId, Feedback feedback) {
        this.transactionId = transactionId;
        this.feedback = feedback;
    }

    /**
     * Get the ID of the transaction for which feedback is provided.
     *
     * @return The transaction ID.
     */
    public Long getTransactionId() {
        return transactionId;
    }

    /**
     * Set the ID of the transaction for which feedback is provided.
     *
     * @param transactionId The transaction ID to set.
     */
    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }

    /**
     * Get the feedback provided for the transaction as a string.
     *
     * @return The feedback as a string ("ALLOWED", "MANUAL_PROCESSING", or "PROHIBITED").
     */
    public String getFeedback() {
        if (feedback == null) {
            return "";  // Return an empty string if feedback is null
        }
        return feedback.toString();
    }

    /**
     * Set the feedback for the transaction.
     *
     * @param feedback The feedback to set (ALLOWED, MANUAL_PROCESSING, or PROHIBITED).
     */
    public void setFeedback(Feedback feedback) {
        this.feedback = feedback;
    }
}