package antifraud.models.userModel;

import jakarta.persistence.*;

import java.util.Collection;
import java.util.Objects;

/**
 * Represents a role entity with attributes for role details of the user.
 */
@Entity
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToMany(mappedBy = "role")
    private Collection<User> users;

    /**
     * Get the users associated with this role.
     *
     * @return The collection of users associated with this role.
     */
    public Collection<User> getUsers() {
        return users;
    }

    /**
     * Set the users associated with this role.
     *
     * @param users The collection of users to associate with this role.
     */
    public void setUsers(Collection<User> users) {
        this.users = users;
    }

    @ManyToMany
    @JoinTable(name = "roles_privileges", joinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "privilege_id", referencedColumnName = "id"))
    private Collection<Privilege> privileges;

    private String name;

    /**
     * Default constructor for the Role class.
     */
    public Role() {
        super();
    }

    /**
     * Constructor for creating a Role with specified properties.
     *
     * @param name The name of the role.
     */
    public Role(final String name) {
        super();
        this.name = name;
    }

    /**
     * Get the ID of the role.
     *
     * @return The ID of the role.
     */
    public Long getId() {
        return id;
    }

    /**
     * Set the ID of the role.
     *
     * @param id The ID of the role.
     */
    public void setId(final Long id) {
        this.id = id;
    }

    /**
     * Get the name of the role.
     *
     * @return The name of the role.
     */
    public String getName() {
        return name;
    }

    /**
     * Set the name of the role.
     *
     * @param name The name of the role.
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * Get the privileges associated with this role.
     *
     * @return The collection of privileges associated with this role.
     */
    public Collection<Privilege> getPrivileges() {
        return privileges;
    }

    /**
     * Set the privileges associated with this role.
     *
     * @param privileges The collection of privileges to associate with this role.
     */
    public void setPrivileges(final Collection<Privilege> privileges) {
        this.privileges = privileges;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Role role = (Role) obj;
        return Objects.equals(id, role.id);
    }

    @Override
    public String toString() {
        return "Role [name=" + name + "]" + "[id=" + id + "]";
    }
}
