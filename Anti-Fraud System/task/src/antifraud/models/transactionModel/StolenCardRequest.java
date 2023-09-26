package antifraud.models.transactionModel;

/**
 * A request model used for adding stolen card data to the anti-fraud system.
 */
public class StolenCardRequest {
    private String number;

    /**
     * Default constructor for the StolenCardRequest class.
     */
    public StolenCardRequest() {
    }

    /**
     * Get the card number.
     *
     * @return The card number.
     */
    public String getNumber() {
        return number;
    }

    /**
     * Set the card number.
     *
     * @param number The card number to set.
     */
    public void setNumber(String number) {
        this.number = number;
    }

}