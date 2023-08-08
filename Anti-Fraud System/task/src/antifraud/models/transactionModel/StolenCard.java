package antifraud.models.transactionModel;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class StolenCard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String number;


    @JsonIgnore
    private int allowedLimit = 200;

    @JsonIgnore
    private int manualLimit = 1500;

    public StolenCard() {
    }

    public StolenCard(Long id, String number) {
        this.id = id;
        this.number = number;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public int getAllowedLimit() {
        return allowedLimit;
    }

    public void setAllowedLimit(int allowedLimit) {
        this.allowedLimit = allowedLimit;
    }

    public int getManualLimit() {
        return manualLimit;
    }

    public void setManualLimit(int manualLimit) {
        this.manualLimit = manualLimit;
    }

    @Override
    public String toString() {
        return "StolenCard{" +
                "id=" + id +
                ", number='" + number + '\'' +
                '}';
    }
}