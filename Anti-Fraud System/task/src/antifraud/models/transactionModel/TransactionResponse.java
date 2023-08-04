package antifraud.models.transactionModel;

public class TransactionResponse {
    private Result result;
    private String info;

    public TransactionResponse() {
    }

    public TransactionResponse(Result result, String info) {
        this.result = result;
        this.info = info;
    }


    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
