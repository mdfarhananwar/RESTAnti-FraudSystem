package antifraud.services;

import antifraud.dao.userDao.UserRepository;
import antifraud.models.userModel.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleService roleService;

    @Autowired
    public UserService(UserRepository userRepository, RoleService roleService) {
        this.userRepository = userRepository;
        this.roleService = roleService;
    }

    /**
     * Register a new user.
     *
     * @param user The user object containing name, username, and password.
     * @return ResponseEntity with status and user information.
     */
    public ResponseEntity<UserResponseDTO> registerUser(User user) {
        // Check if any required fields are null
        if (isInvalidUser(user)) {
            return ResponseEntity.badRequest().build();
        }

        // Check if the user already exists
        if (userExists(user.getUsername())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        // Encode the user's password
        user.setPassword(encodePassword(user.getPassword()));

        // Determine the role and operation for the user
        String userRole = decideUserRole();

        // Save the user in the database
        User savedUser = saveUserWithRole(user, userRole);

        // Create a UserResponseDTO from the saved user
        UserResponseDTO userResponse = mapToUserResponseDTO(savedUser);

        // Respond with HTTP CREATED and the user information
        return ResponseEntity.status(HttpStatus.CREATED).body(userResponse);
    }

    /**
     * Get a list of all registered users.
     *
     * @return ResponseEntity with the status and a list of user responses.
     */
    public ResponseEntity<List<UserResponseDTO>> getListOfUsers() {
        List<User> all = userRepository.findAll();
        List<UserResponseDTO> userResponses = all.stream()
                .map(UserResponseDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(userResponses);
    }


    /**
     * Update the role of a user.
     *
     * @param userRole The user role information containing username and role.
     * @return ResponseEntity with status and user information.
     */
    public ResponseEntity<UserResponseDTO> updateRole(UserRole userRole) {
        // Check if the user exists
        if (!userExists(userRole.getUsername())) {
            return ResponseEntity.notFound().build();
        }

        User existingUser = findUserByUsername(userRole.getUsername());
        String newRoleName = userRole.getRole();

        // Check if Role is valid (Either it is "SUPPORT" or "MERCHANT")
        if (!isValidRole(newRoleName)) {
            return ResponseEntity.badRequest().build();
        }

        // Check if assigned role is already provided to the user,
        // respond with the HTTP Conflict status (409);
        if (isRoleAlreadyAssigned(existingUser, newRoleName)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        //Update and Save the Role in the Database
        Role newRole = new Role(newRoleName);
        roleService.saveRole(newRole);

        existingUser.setRole(newRole);
        saveUser(existingUser);

        // Update the role in the user response
        existingUser.getRole().setName(newRoleName);

        // Respond with HTTP OK and the updated user information
        return ResponseEntity.ok(new UserResponseDTO(existingUser));
    }

    /**
     * Update user access (lock/unlock) based on the provided AccessRequest.
     *
     * @param accessRequest    The access request object containing username and operation.
     * @param authentication   The authentication object for the currently logged-in user.
     * @return ResponseEntity with the status and a response message.
     */
    public ResponseEntity<Map<String, String>> updateAccess(
            AccessRequest accessRequest,
            Authentication authentication
    ) {
        String targetUsername = accessRequest.getUsername();
        Operation operation = accessRequest.getOperation();

        if (!userExists(targetUsername)) {
            // User not found
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        // Get the username and role of the authenticated user
        String authenticatedUsername = authentication.getName();
        User authenticatedUser = findUserByUsername(authenticatedUsername);

        // Check for invalid operations
        if (isAdministratorLocked(targetUsername, operation)) {
            // Administrators cannot be locked
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        if (isUnauthorizedUnlock(operation, authenticatedUser)) {
            // Only administrators can unlock users
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        // Update the user's access operation (lock/unlock)
        updateAccessOperation(targetUsername, operation);

        // Get the response body from helper method
        Map<String, String> responseBody = createResponse(operation, targetUsername);

        // Respond with HTTP OK and the response body
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }

    /**
     * Delete a user by username.
     *
     * @param username The username of the user to be deleted.
     * @return ResponseEntity with the status and a response message.
     */
    @Transactional
    public ResponseEntity<UserResponseDelete> deleteUser(String username) {
        // Check if the user exists
        if (!userExists(username)) {
            return ResponseEntity.notFound().build();
        }

        User userToDelete = findUserByUsername(username);

        // Check if the user is an administrator and cannot delete oneself
        if (isAdmin(userToDelete)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        // Delete the user
        userRepository.deleteByUsernameIgnoreCase(username);

        // Prepare the response message
        String responseMessage = "Deleted successfully!";
        UserResponseDelete response = new UserResponseDelete(username, responseMessage);

        // Respond with HTTP OK and the response body
        return ResponseEntity.ok(response);
    }

    // Helper Methods :-----------------------------------------------------------------------------------------------

    // Helper methods for method - registerUser

    private boolean isInvalidUser(User user) {
        return user.getUsername() == null || user.getName() == null || user.getPassword() == null;
    }

    private boolean userExists(String username) {
        return userRepository.existsByUsernameIgnoreCase(username);
    }

    private String encodePassword(String password) {
        return passwordEncoder().encode(password);
    }

    private String decideUserRole() {
        return count() == 0 ? "ADMINISTRATOR" : "MERCHANT";
    }

    private User saveUserWithRole(User user, String role) {
        Role userRole = new Role(role);
        roleService.saveRole(userRole);
        user.setRole(userRole);
        user.setOperation(Objects.equals(role, "ADMINISTRATOR") ? Operation.UNLOCK : Operation.LOCK);
        return saveUser(user);
    }


    // Helper methods for method - updateRole

    private boolean isValidRole(String role) {
        return role.equals("SUPPORT") || role.equals("MERCHANT");
    }

    private boolean isRoleAlreadyAssigned(User existingUser, String roleToBeAssigned) {
        return existingUser.getRole().getName().equals(roleToBeAssigned);
    }

    // Helper methods for method - updateAccess

    private boolean isAdministratorLocked(String targetUsername, Operation operation) {
        User targetUser = findUserByUsername(targetUsername);
        return isAdmin(targetUser) && operation == Operation.LOCK;
    }

    private boolean isUnauthorizedUnlock(Operation operation, User authenticatedUser) {
        return operation == Operation.UNLOCK && !authenticatedUser.getRole().getName().equals("ADMINISTRATOR");
    }

    private void updateAccessOperation(String targetUsername, Operation operation) {
        User targetUser = findUserByUsername(targetUsername);
        targetUser.setOperation(operation);
        saveUser(targetUser);
    }

    //Create the Response Body
    private Map<String, String> createResponse(Operation operation, String targetUsername) {
        // Prepare the response message
        String status = (operation == Operation.LOCK) ? "locked" : "unlocked";
        String responseMessage = "User " + targetUsername + " " + status + "!";

        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("status", responseMessage);
        return responseBody;
    }

    //Helper Methods for common methods

    public User findUserByUsername(String username) {
        Optional<User> optionalUser = userRepository.findByUsername(username);
        return optionalUser.orElse(null);
    }

    @Transactional
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public UserResponseDTO mapToUserResponseDTO(User user) {
        return new UserResponseDTO(user);
    }

    public Long count() {
        return userRepository.count();
    }


    public boolean isAdmin(User user) {
        String roleName = user.getRole().getName();
        return roleName.equals("ADMINISTRATOR");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
