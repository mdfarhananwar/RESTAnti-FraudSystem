package antifraud.models.transactionModel;

import java.time.LocalDateTime;
import java.util.Date;

public class TransactionRequest {
    private Long amount;
    private String ip;
    private String number;
    private Region region;
    private LocalDateTime date;

    public TransactionRequest() {
    }


    public TransactionRequest(Long amount, String ip, String number, Region region, LocalDateTime date) {
        this.amount = amount;
        this.ip = ip;
        this.number = number;
        this.region = region;
        this.date = date;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    public LocalDateTime getDate() {
        return date;
    }

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
