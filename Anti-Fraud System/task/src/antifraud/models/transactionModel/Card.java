package antifraud.models.transactionModel;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long transactionId;
    private String number;
    private int maxAllowed = 200;
    private int maxManual = 1500;

    public Card(String number) {
        this.number = number;
    }

    public Card() {
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public int getMaxAllowed() {
        return maxAllowed;
    }

    public void setMaxAllowed(int maxAllowed) {
        this.maxAllowed = maxAllowed;
    }

    public int getMaxManual() {
        return maxManual;
    }

    public void setMaxManual(int maxManual) {
        this.maxManual = maxManual;
    }

    public Long getTransactionId() {
        return transactionId;
    }

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
