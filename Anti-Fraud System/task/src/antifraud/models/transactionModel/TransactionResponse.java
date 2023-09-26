package antifraud.models.transactionModel;

/**
 * A response model used for providing information about the result of a transaction in the anti-fraud system.
 */
public class TransactionResponse {
    private Result result;
    private String info;

    /**
     * Default constructor for the TransactionResponse class.
     */
    public TransactionResponse() {
    }

    /**
     * Constructor for creating a TransactionResponse with specified properties.
     *
     * @param result The result of the transaction (ALLOWED, MANUAL_PROCESSING, or PROHIBITED).
     * @param info Additional information or reasons for the transaction result.
     */
    public TransactionResponse(Result result, String info) {
        this.result = result;
        this.info = info;
    }

    /**
     * Get the result of the transaction.
     *
     * @return The transaction result (ALLOWED, MANUAL_PROCESSING, or PROHIBITED).
     */
    public Result getResult() {
        return result;
    }

    /**
     * Set the result of the transaction.
     *
     * @param result The transaction result to set (ALLOWED, MANUAL_PROCESSING, or PROHIBITED).
     */
    public void setResult(Result result) {
        this.result = result;
    }

    /**
     * Get additional information or reasons for the transaction result.
     *
     * @return Additional information about the transaction result.
     */
    public String getInfo() {
        return info;
    }

    /**
     * Set additional information or reasons for the transaction result.
     *
     * @param info Additional information about the transaction result.
     */
    public void setInfo(String info) {
        this.info = info;
    }
}