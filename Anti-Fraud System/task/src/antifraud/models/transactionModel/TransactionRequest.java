package antifraud.models.transactionModel;

import java.time.LocalDateTime;

/**
 * A request model used for creating a transaction in the anti-fraud system.
 */
public class TransactionRequest {
    private Long amount;
    private String ip;
    private String number;
    private Region region;
    private LocalDateTime date;

    /**
     * Default constructor for the TransactionRequest class.
     */
    public TransactionRequest() {
    }

    /**
     * Constructor for creating a TransactionRequest with specified properties.
     *
     * @param amount The transaction amount.
     * @param ip The IP address associated with the transaction.
     * @param number The card number used in the transaction.
     * @param region The region associated with the transaction.
     * @param date The date and time of the transaction.
     */
    public TransactionRequest(Long amount, String ip, String number, Region region, LocalDateTime date) {
        this.amount = amount;
        this.ip = ip;
        this.number = number;
        this.region = region;
        this.date = date;
    }

    /**
     * Get the transaction amount.
     *
     * @return The transaction amount.
     */
    public Long getAmount() {
        return amount;
    }

    /**
     * Set the transaction amount.
     *
     * @param amount The transaction amount to set.
     */
    public void setAmount(Long amount) {
        this.amount = amount;
    }

    /**
     * Get the IP address associated with the transaction.
     *
     * @return The IP address.
     */
    public String getIp() {
        return ip;
    }

    /**
     * Set the IP address associated with the transaction.
     *
     * @param ip The IP address to set.
     */
    public void setIp(String ip) {
        this.ip = ip;
    }

    /**
     * Get the card number used in the transaction.
     *
     * @return The card number.
     */
    public String getNumber() {
        return number;
    }

    /**
     * Set the card number used in the transaction.
     *
     * @param number The card number to set.
     */
    public void setNumber(String number) {
        this.number = number;
    }

    /**
     * Get the region associated with the transaction.
     *
     * @return The region.
     */
    public Region getRegion() {
        return region;
    }

    /**
     * Set the region associated with the transaction.
     *
     * @param region The region to set.
     */
    public void setRegion(Region region) {
        this.region = region;
    }

    /**
     * Get the date and time of the transaction.
     *
     * @return The date and time.
     */
    public LocalDateTime getDate() {
        return date;
    }

    /**
     * Set the date and time of the transaction.
     *
     * @param date The date and time to set.
     */
    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "TransactionRequest{" +
                "amount=" + amount +
                ", ip='" + ip + '\'' +
                ", number='" + number + '\'' +
                ", region=" + region +
                ", date=" + date +
                '}';
    }
}