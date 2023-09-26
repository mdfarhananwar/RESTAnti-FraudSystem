package antifraud.controller;

import antifraud.models.userModel.*;
import antifraud.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Controller for managing registration of new users and giving them role accordingly.
 */
@RestController
public class RegistrationController {

    private final UserService userService;

    @Autowired
    RegistrationController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Register a new user.
     * <p>
     * POST /api/auth/user
     * Accepts data in JSON format:
     * {
     *   "name": "<String value, not empty>",
     *   "username": "<String value, not empty>",
     *   "password": "<String value, not empty>"
     * }
     * <p>
     * Responds with:
     * - HTTP CREATED status (201) if successful. Response Body :
     * {
     *   "id": <Long value, not empty>,
     *   "name": "<String value, not empty>",
     *   "username": "<String value, not empty>",
     *   "role": "<String value, not empty>"
     * }
     * - HTTP CONFLICT status (409) if registration fails.
     * - HTTP BAD REQUEST status (400) if the request data is incorrect.
     *
     * @param user The user information to register.
     * @return ResponseEntity containing the result of the registration.
     */
    @PostMapping("/api/auth/user")
    public ResponseEntity<UserResponseDTO> register(@RequestBody User user) {
        return userService.registerUser(user);
    }

    /**
     * Delete a user by username.
     * <p>
     * DELETE /api/auth/user/{username}
     * Deletes the user specified by {username}.
     * Responds with:
     * - HTTP OK status (200) and a message indicating successful deletion. Response Body :
     * {
     *    "username": "<username>",
     *    "status": "Deleted successfully!"
     * }
     * - HTTP NOT FOUND status (404) if the user is not found.
     *
     * @param username The username of the user to be deleted.
     * @return ResponseEntity containing the result of the user deletion.
     */
    @DeleteMapping("/api/auth/user/{username}")
    public ResponseEntity<UserResponseDelete> deleteByUsername(@PathVariable String username) {
        return userService.deleteUser(username);
    }

    /**
     * Change the role of a user.
     * <p>
     * PUT /api/auth/role
     * Accepts data in JSON format:
     * {
     *   "username": "<String value, not empty>",
     *   "role": "<String value, not empty>"
     * }
     * Responds with:
     * - HTTP OK status (200) if successful. Response Body :
     * {
     *    "id": <Long value, not empty>,
     *    "name": "<String value, not empty>",
     *    "username": "<String value, not empty>",
     *    "role": "<String value, not empty>"
     * }
     * - HTTP CONFLICT status (409) if the role change fails.
     * - HTTP BAD REQUEST status (400) if the request data is incorrect.
     *
     * @param userRole The user role information for role change.
     * @return ResponseEntity containing the result of the role change.
     */
    @PutMapping("/api/auth/role")
    public ResponseEntity<UserResponseDTO> changeRole(@RequestBody UserRole userRole) {
        return userService.updateRole(userRole);
    }

    /**
     * Change user access.
     * <p>
     * PUT /api/auth/access
     * Accepts data in JSON format:
     * {
     *   "username": "<String value, not empty>",
     *   "operation": "<LOCK or UNLOCK>"
     * }
     * Responds with:
     * - HTTP OK status (200) if successful. Response Body :
     * {
     *     "status": "User <username> <[locked, unlocked]>!"
     * }
     * - HTTP FORBIDDEN status (403) if access control fails.
     * - HTTP NOT FOUND status (404) if the user is not found.
     * - HTTP BAD REQUEST status (400) if the request data is incorrect.
     *
     * @param accessRequest The access request data.
     * @param authentication The authentication object for user authorization.
     * @return ResponseEntity containing the result of the access control change.
     */
    @PutMapping("/api/auth/access")
    public ResponseEntity<Map<String, String>> changeAccess(
            @RequestBody AccessRequest accessRequest,
            Authentication authentication) {
        return userService.updateAccess(accessRequest,authentication);
    }

    /**
     * Get a list of all registered users.
     * <p>
     * GET /api/auth/list
     * Responds with:
     * - HTTP OK status (200) and an array of user objects sorted by ID in ascending order. Response Body :
     * [
     *     {
     *         "id": <user1 id>,
     *         "name": "<user1 name>",
     *         "username": "<user1 username>",
     *         "role": "<user1 role>"
     *     },
     *      ...
     *     {
     *         "id": <userN id>,
     *         "name": "<userN name>",
     *         "username": "<userN username>",
     *         "role": "<userN role>"
     *     }
     * ]
     * - An empty JSON array if there are no users.
     *
     * @return ResponseEntity containing the list of registered users.
     */
    @GetMapping("/api/auth/list")
    public ResponseEntity<List<UserResponseDTO>> getUserList() {
        return userService.getListOfUsers();
    }



}
