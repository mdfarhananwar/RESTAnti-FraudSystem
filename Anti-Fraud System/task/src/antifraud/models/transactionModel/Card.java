package antifraud.models.transactionModel;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

/**
 * Represents a card entity with attributes for card details and transaction limits.
 */
@Entity
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long transactionId; // Transaction ID associated with the card
    private String number; // Card number
    private int maxAllowed = 200; // Maximum allowed transaction amount
    private int maxManual = 1500; // Maximum amount for transactions requiring manual processing

    /**
     * Constructs a Card object with the specified card number.
     *
     * @param number The card number.
     */
    public Card(String number) {
        this.number = number;
    }

    /**
     * Default constructor for the Card class.
     */
    public Card() {
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
     * @param number The card number.
     */
    public void setNumber(String number) {
        this.number = number;
    }

    /**
     * Get the maximum allowed transaction amount for this card.
     *
     * @return The maximum allowed transaction amount.
     */
    public int getMaxAllowed() {
        return maxAllowed;
    }

    /**
     * Set the maximum allowed transaction amount for this card.
     *
     * @param maxAllowed The maximum allowed transaction amount.
     */
    public void setMaxAllowed(int maxAllowed) {
        this.maxAllowed = maxAllowed;
    }

    /**
     * Get the maximum amount for transactions requiring manual processing for this card.
     *
     * @return The maximum amount for manual processing.
     */
    public int getMaxManual() {
        return maxManual;
    }

    /**
     * Set the maximum amount for transactions requiring manual processing for this card.
     *
     * @param maxManual The maximum amount for manual processing.
     */
    public void setMaxManual(int maxManual) {
        this.maxManual = maxManual;
    }

    /**
     * Get the transaction ID associated with this card.
     *
     * @return The transaction ID.
     */
    public Long getTransactionId() {
        return transactionId;
    }

    /**
     * Set the transaction ID associated with this card.
     *
     * @param transactionId The transaction ID.
     */
    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }

    @Override
    public String toString() {
        return "Card{" +
                "number='" + number + '\'' +
                ", maxAllowed=" + maxAllowed +
                ", maxManual=" + maxManual +
                '}';
    }
}