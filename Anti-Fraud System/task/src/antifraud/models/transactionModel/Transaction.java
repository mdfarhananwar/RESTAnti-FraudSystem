package antifraud.models.transactionModel;

public class Transaction {
    private Long amount;
    private String ip;
    private String number;

    public Transaction() {
    }

    public Transaction(Long amount, IpAddress ipAddress, StolenCard stolenCard) {
        this.amount = amount;
        this.ip = ipAddress.getIp();
        this.number = stolenCard.getNumber();
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
    @Override
    public String toString() {
        return "Transaction{" +
                "amount=" + amount +
                ", ip='" + ip + '\'' +
                ", number='" + number + '\'' +
                '}';
    }

}
