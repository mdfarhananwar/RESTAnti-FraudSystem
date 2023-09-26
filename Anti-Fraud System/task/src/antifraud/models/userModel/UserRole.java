package antifraud.models.userModel;

/**
 * A class representing a user's role in the anti-fraud system.
 */
public class UserRole {
    private String username;
    private String role;

    /**
     * Default constructor for the UserRole class.
     */
    public UserRole() {
    }

    /**
     * Constructor for creating a UserRole based on a User entity.
     *
     * @param user The User entity from which to create the UserRole.
     */
    public UserRole(User user) {
        this.username = user.getUsername();
        this.role = user.getRole().getName();
    }

    /**
     * Get the username of the user.
     *
     * @return The username of the user.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Set the username of the user.
     *
     * @param username The username of the user.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Get the role of the user.
     *
     * @return The role of the user.
     */
    public String getRole() {
        return role;
    }

    /**
     * Set the role of the user.
     *
     * @param role The role of the user.
     */
    public void setRole(String role) {
        this.role = role;
    }
}
