package antifraud.models.transactionModel;

/**
 * Represents a response model for resource deletion operations.
 */
public class DeleteResponse {
    private String status; // A status message indicating the result of the deletion operation

    /**
     * Default constructor for the DeleteResponse class.
     */
    public DeleteResponse() {
    }

    /**
     * Constructs a DeleteResponse object with the specified status message.
     *
     * @param status The status message indicating the result of the deletion operation.
     */
    public DeleteResponse(String status) {
        this.status = status;
    }

    /**
     * Get the status message indicating the result of the deletion operation.
     *
     * @return The status message.
     */
    public String getStatus() {
        return status;
    }

    /**
     * Set the status message indicating the result of the deletion operation.
     *
     * @param status The status message.
     */
    public void setStatus(String status) {
        this.status = status;
    }
}