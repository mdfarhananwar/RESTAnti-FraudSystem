package antifraud.security;


import antifraud.models.userModel.Operation;
import antifraud.models.userModel.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * Custom implementation of the Spring Security UserDetails interface.
 * This class is responsible for representing user details for authentication and authorization purposes
 * in a Spring Security-based application.
 */
public class UserDetailsImpl implements UserDetails {

    private final String username;
    private final String password;
    private boolean accountNonLocked;
    private final List<GrantedAuthority> rolesAndAuthorities;
    @Autowired
    private Operation operation;

    /**
     * Constructor to create UserDetailsImpl from a User entity.
     *
     * @param user The User entity from which UserDetailsImpl is created.
     */
    public UserDetailsImpl(User user) {
        this.username = user.getUsername();
        this.password = user.getPassword();
        String roleWithPrefix = "ROLE_" + user.getRole().getName();
        rolesAndAuthorities = List.of(new SimpleGrantedAuthority(roleWithPrefix));
    }

    /**
     * Returns the authorities granted to the user. In this implementation, it includes the user's role.
     *
     * @return A collection of granted authorities.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return rolesAndAuthorities;
    }

    /**
     * Returns the password used to authenticate the user.
     *
     * @return The user's password.
     */
    @Override
    public String getPassword() {
        return password;
    }

    /**
     * Returns the username used to authenticate the user.
     *
     * @return The user's username.
     */
    @Override
    public String getUsername() {
        return username;
    }

    /**
     * Indicates whether the user's account has expired. In this implementation, it always returns true.
     *
     * @return True if the user's account is not expired.
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Indicates whether the user's account is locked. In this implementation, it depends on the 'operation' value.
     *
     * @return True if the user's account is not locked.
     */
    @Override
    public boolean isAccountNonLocked() {
        return operation == Operation.UNLOCK;
    }

    /**
     * Indicates whether the user's credentials (password) have expired. In this implementation, it always returns true.
     *
     * @return True if the user's credentials are not expired.
     */
    public void setAccountNonLocked(boolean accountNonLocked) {
        this.accountNonLocked = accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Indicates whether the user is enabled or disabled. In this implementation, it always returns true.
     *
     * @return True if the user is enabled.
     */
    @Override
    public boolean isEnabled() {
        return true;
    }
}
