package antifraud.models.userModel;

/**
 * A class representing a response for deleting a user in the anti-fraud system.
 */
public class UserResponseDelete {
    private String username;
    private String status;

    /**
     * Default constructor for the UserResponseDelete class.
     */
    public UserResponseDelete() {

    }
    /**
     * Constructor for creating a UserResponseDelete with specified properties.
     *
     * @param username The username of the deleted user.
     * @param status   The status of the deletion operation (e.g., success or failure).
     */
    public UserResponseDelete(String username, String status) {
        this.username = username;
        this.status = status;
    }

    /**
     * Get the username of the deleted user.
     *
     * @return The username of the deleted user.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Set the username of the deleted user.
     *
     * @param username The username of the deleted user.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Get the status of the deletion operation.
     *
     * @return The status of the deletion operation.
     */
    public String getStatus() {
        return status;
    }

    /**
     * Set the status of the deletion operation.
     *
     * @param status The status of the deletion operation.
     */
    public void setStatus(String status) {
        this.status = status;
    }
}
