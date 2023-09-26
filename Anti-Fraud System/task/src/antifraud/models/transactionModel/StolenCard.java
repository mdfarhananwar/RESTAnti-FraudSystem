package antifraud.models.transactionModel;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

/**
 * Represents a stolen card entity with attributes for card details.
 */
@Entity
public class StolenCard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String number; // Card number of the stolen card

    /**
     * Default constructor for the StolenCard class.
     */
    public StolenCard() {
    }

    /**
     * Constructs a StolenCard object with the specified ID and card number.
     *
     * @param id     The ID of the stolen card.
     * @param number The card number of the stolen card.
     */
    public StolenCard(Long id, String number) {
        this.id = id;
        this.number = number;
    }

    /**
     * Get the ID of the stolen card.
     *
     * @return The ID of the stolen card.
     */
    public Long getId() {
        return id;
    }

    /**
     * Set the ID of the stolen card.
     *
     * @param id The ID of the stolen card.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Get the card number of the stolen card.
     *
     * @return The card number of the stolen card.
     */
    public String getNumber() {
        return number;
    }

    /**
     * Set the card number of the stolen card.
     *
     * @param number The card number of the stolen card.
     */
    public void setNumber(String number) {
        this.number = number;
    }

    @Override
    public String toString() {
        return "StolenCard{" +
                "id=" + id +
                ", number='" + number + '\'' +
                '}';
    }
}