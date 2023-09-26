package antifraud.models.userModel;

/**
 * A class representing a request for changing the access permissions of a user in the anti-fraud system.
 */
public class AccessRequest {
    private String username;
    private Operation operation;

    /**
     * Default constructor for the AccessRequest class.
     */
    public AccessRequest() {
    }

    /**
     * Constructor for creating an AccessRequest with specified properties.
     *
     * @param username  The username of the user for whom access permissions are to be changed.
     * @param operation The operation to perform (e.g., granting or revoking access).
     */
    public AccessRequest(String username, Operation operation) {
        this.username = username;
        this.operation = operation;
    }

    /**
     * Get the username of the user for whom access permissions are to be changed.
     *
     * @return The username of the user.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Set the username of the user for whom access permissions are to be changed.
     *
     * @param username The username of the user.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Get the operation to perform (e.g., granting or revoking access).
     *
     * @return The operation to perform.
     */
    public Operation getOperation() {
        return operation;
    }

    /**
     * Set the operation to perform (e.g., granting or revoking access).
     *
     * @param operation The operation to perform.
     */
    public void setOperation(Operation operation) {
        this.operation = operation;
    }
}