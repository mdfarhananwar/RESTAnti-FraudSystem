package antifraud.models.userModel;

public class AccessRequest {
    private String username;
    private Operation operation;

    public AccessRequest() {
    }

    public AccessRequest(String username, Operation operation) {
        this.username = username;
        this.operation = operation;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Operation getOperation() {
        return operation;
    }

    public void setOperation(Operation operation) {
        this.operation = operation;
    }
}
