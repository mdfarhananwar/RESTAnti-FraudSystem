package antifraud.models.userModel;

import jakarta.persistence.*;

/**
 * Represents a user entity with attributes for user details and their role and operations.
 */
@Entity
@Table(name = "`user`")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String username;
    private String password;

    /**
     * Default constructor for the User class.
     */
    public User() {
    }

    /**
     * Constructor for creating a User with specified properties.
     *
     * @param name     The name of the user.
     * @param username The username of the user.
     * @param password The password of the user.
     */
    public User(String name, String username, String password) {
        this.name = name;
        this.username = username;
        this.password = password;
    }
    @ManyToOne(fetch = FetchType.EAGER) // Since each user has one role, use ManyToOne association
    @JoinColumn(name = "role_id") // The column in "user" table that references the "role" table
    private Role role;
    @Enumerated(EnumType.STRING)
    @Column(name = "user_operation")
    private Operation operation;

    // Getters and setters for all fields

    /**
     * Get the ID of the user.
     *
     * @return The ID of the user.
     */
    public Long getId() {
        return id;
    }

    public Operation getOperation() {
        return operation;
    }

    public void setOperation(Operation operation) {
        this.operation = operation;
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
     * Get the password of the user.
     *
     * @return The password of the user.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Set the password of the user.
     *
     * @param password The password of the user.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Get the role associated with the user.
     *
     * @return The role associated with the user.
     */
    public Role getRole() {
        return role;
    }

    /**
     * Set the role associated with the user.
     *
     * @param role The role associated with the user.
     */
    public void setRole(final Role role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", role=" + role +
                '}';
    }
}