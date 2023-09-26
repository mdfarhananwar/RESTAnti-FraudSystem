package antifraud.models.userModel;

/**
 * A Data Transfer Object (DTO) representing a user's response in the anti-fraud system.
 */
public class UserResponseDTO {
    private Long id;
    private String name;
    private String username;
    private String role;

    /**
     * Default constructor for the UserResponseDTO class.
     */
    public UserResponseDTO() {
    }

    /**
     * Constructor for creating a UserResponseDTO based on a User entity.
     *
     * @param user The User entity from which to create the DTO.
     */
    public UserResponseDTO(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.username = user.getUsername();
        this.role = user.getRole().getName();
    }

    /**
     * Get the ID of the user.
     *
     * @return The ID of the user.
     */
    public Long getId() {
        return id;
    }

    /**
     * Set the ID of the user.
     *
     * @param id The ID of the user.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Get the name of the user.
     *
     * @return The name of the user.
     */
    public String getName() {
        return name;
    }

    /**
     * Set the name of the user.
     *
     * @param name The name of the user.
     */
    public void setName(String name) {
        this.name = name;
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
