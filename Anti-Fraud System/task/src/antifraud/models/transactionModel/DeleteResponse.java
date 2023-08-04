package antifraud.models.transactionModel;

public class DeleteResponse {
    private String status;

    public DeleteResponse() {
    }

    public DeleteResponse(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
