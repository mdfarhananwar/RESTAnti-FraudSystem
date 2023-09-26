package antifraud.security;

import antifraud.models.userModel.Privilege;
import antifraud.models.userModel.Role;
import antifraud.models.userModel.User;
import antifraud.services.UserService;
import jakarta.transaction.Transactional;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Implementation of the Spring Security UserDetailsService interface for loading user details during authentication.
 */
@Service("userDetailsService")
@Transactional
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserService userService;

    /**
     * Constructor to create an instance of UserDetailsServiceImpl with a UserService dependency.
     *
     * @param userService The UserService used to retrieve user details.
     */
    public UserDetailsServiceImpl(UserService userService) {
        this.userService = userService;
    }

    /**
     * Load user details by username. This method is called during authentication.
     *
     * @param username The username of the user.
     * @return UserDetails object containing user details.
     * @throws UsernameNotFoundException If the user with the provided username is not found.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.findUserByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("Not found: " + username);
        }
        String roleWithPrefix = "ROLE_" + user.getRole().getName();
        List<SimpleGrantedAuthority> grantedAuthorities = Collections.singletonList(new SimpleGrantedAuthority(roleWithPrefix));
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), grantedAuthorities);
    }

    /**
     * Get authorities (privileges) for a user based on their roles.
     *
     * @param roles A collection of roles assigned to the user.
     * @return A collection of granted authorities (privileges).
     */
    private Collection<? extends GrantedAuthority> getAuthorities(final Collection<Role> roles) {
        return getGrantedAuthorities(getPrivileges(roles));
    }

    /**
     * Get privileges associated with a collection of roles.
     *
     * @param roles A collection of roles.
     * @return A list of privilege names.
     */
    private List<String> getPrivileges(final Collection<Role> roles) {
        final List<String> privileges = new ArrayList<>();
        final List<Privilege> collection = new ArrayList<>();
        for (final Role role : roles) {
            privileges.add(role.getName());
            collection.addAll(role.getPrivileges());
        }
        for (final Privilege item : collection) {
            privileges.add(item.getName());
        }

        return privileges;
    }

    /**
     * Convert a list of privilege names into a list of GrantedAuthority objects.
     *
     * @param privileges A list of privilege names.
     * @return A list of GrantedAuthority objects.
     */
    private List<GrantedAuthority> getGrantedAuthorities(final List<String> privileges) {
        final List<GrantedAuthority> authorities = new ArrayList<>();
        for (final String privilege : privileges) {
            authorities.add(new SimpleGrantedAuthority(privilege));
        }
        return authorities;
    }

}
