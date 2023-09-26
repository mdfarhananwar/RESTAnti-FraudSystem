package antifraud.models.userModel;

import jakarta.persistence.*;

import java.util.Collection;

/**
 * Represents a privilege entity with attributes for defining user privileges in the anti-fraud system.
 */
@Entity
public class Privilege {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    /**
     * The roles associated with this privilege.
     */
    @ManyToMany(mappedBy = "privileges")
    private Collection<Role> roles;

    /**
     * Default constructor for the Privilege class.
     */
    public Privilege() {
        super();
    }

    /**
     * Constructor for creating a Privilege with a specified name.
     *
     * @param name The name of the privilege.
     */
    public Privilege(final String name) {
        super();
        this.name = name;
    }

    /**
     * Get the ID of the privilege.
     *
     * @return The ID of the privilege.
     */
    public Long getId() {
        return id;
    }

    /**
     * Set the ID of the privilege.
     *
     * @param id The ID of the privilege.
     */
    public void setId(final Long id) {
        this.id = id;
    }

    /**
     * Get the name of the privilege.
     *
     * @return The name of the privilege.
     */
    public String getName() {
        return name;
    }

    /**
     * Get the roles associated with this privilege.
     *
     * @return The collection of roles associated with this privilege.
     */
    public Collection<Role> getRoles() {
        return roles;
    }

    /**
     * Set the roles associated with this privilege.
     *
     * @param roles The collection of roles to associate with this privilege.
     */
    public void setRoles(final Collection<Role> roles) {
        this.roles = roles;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getName() == null) ? 0 : getName().hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Privilege other = (Privilege) obj;
        if (getName() == null) {
            return other.getName() == null;
        } else return getName().equals(other.getName());
    }

}
