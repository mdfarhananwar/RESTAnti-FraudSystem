package antifraud.models.transactionModel;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;

/**
 * Represents a transaction entity with details such as amount, IP address, card number, region, date, result, and feedback.
 */
@Entity
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transactionId; // Unique identifier for the transaction

    private Long amount; // Transaction amount

    private String ip; // IP address associated with the transaction

    private String number; // Card number associated with the transaction

    private Region region; // Region of the transaction

    private LocalDateTime date; // Date and time of the transaction

    private Result result; // Result of the transaction (ALLOWED, PROHIBITED, MANUAL_PROCESSING)

    private Feedback feedback; // Feedback associated with the transaction

    /**
     * Default constructor for the Transaction class.
     */
    public Transaction() {
    }

    /**
     * Constructs a Transaction object with specified transaction details.
     *
     * @param amount  The transaction amount.
     * @param ip      The IP address associated with the transaction.
     * @param number  The card number associated with the transaction.
     * @param region  The region of the transaction.
     * @param date    The date and time of the transaction.
     */
    public Transaction(Long amount, String ip, String number, Region region, LocalDateTime date) {
        this.amount = amount;
        this.ip = ip;
        this.number = number;
        this.region = region;
        this.date = date;
    }

    /**
     * Get the unique identifier of the transaction.
     *
     * @return The transaction identifier.
     */
    public Long getTransactionId() {
        return transactionId;
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
     * @param amount The transaction amount.
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
     * @param ip The IP address.
     */
    public void setIp(String ip) {
        this.ip = ip;
    }

    /**
     * Get the card number associated with the transaction.
     *
     * @return The card number.
     */
    public String getNumber() {
        return number;
    }

    /**
     * Set the card number associated with the transaction.
     *
     * @param number The card number.
     */
    public void setNumber(String number) {
        this.number = number;
    }

    /**
     * Get the region of the transaction.
     *
     * @return The transaction region.
     */
    public Region getRegion() {
        return region;
    }

    /**
     * Set the region of the transaction.
     *
     * @param region The transaction region.
     */
    public void setRegion(Region region) {
        this.region = region;
    }

    /**
     * Get the date and time of the transaction.
     *
     * @return The transaction date and time.
     */
    public LocalDateTime getDate() {
        return date;
    }

    /**
     * Set the date and time of the transaction.
     *
     * @param date The transaction date and time.
     */
    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    /**
     * Get the result of the transaction (ALLOWED, PROHIBITED, MANUAL_PROCESSING).
     *
     * @return The transaction result.
     */
    public Result getResult() {
        return result;
    }

    /**
     * Set the result of the transaction (ALLOWED, PROHIBITED, MANUAL_PROCESSING).
     *
     * @param result The transaction result.
     */
    public void setResult(Result result) {
        this.result = result;
    }

    /**
     * Get the feedback associated with the transaction as a string.
     *
     * @return The transaction feedback as a string.
     */
    public String getFeedback() {
        if (feedback == null) {
            return "";  // Return an empty string if feedback is null
        }
        return feedback.toString();  // Return the string representation of feedback
    }

    /**
     * Set the feedback associated with the transaction.
     *
     * @param feedback The transaction feedback.
     */
    public void setFeedback(Feedback feedback) {
        this.feedback = feedback;
    }


    @Override
    public String toString() {
        return "Transaction{" +
                "amount=" + amount +
                ", ip='" + ip + '\'' +
                ", number='" + number + '\'' +
                ", region='" + region + '\'' +
                ", date=" + date +
                '}';
    }

}