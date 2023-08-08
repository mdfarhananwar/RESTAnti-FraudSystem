package antifraud.models.transactionModel;

public class TransactionRequestFeedback {
    private Long transactionId;
    private Feedback feedback;

    public TransactionRequestFeedback() {
    }

    public TransactionRequestFeedback(Long transactionId, Feedback feedback) {
        this.transactionId = transactionId;
        this.feedback = feedback;
    }

    public Long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }

    public String getFeedback() {
        if (feedback == null) {
            return "";  // Return an empty string if feedback is null
        }
        return feedback.toString();
    }

    public void setFeedback(Feedback feedback) {
        this.feedback = feedback;
    }
}
