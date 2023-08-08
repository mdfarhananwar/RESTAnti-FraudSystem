package antifraud.controller;

import antifraud.models.userModel.*;
import antifraud.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class RegistrationController {

    private final UserService userService;

    @Autowired
    RegistrationController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/api/auth/user")
    public ResponseEntity<UserResponseDTO> register(@RequestBody User user) {
        return userService.registerUser(user);
    }
    @PutMapping("/api/auth/role")
    public ResponseEntity<UserResponseDTO> changeRole(@RequestBody UserRole userRole) {
        return userService.updateRole(userRole);
    }
    @PutMapping("/api/auth/access")
    public ResponseEntity<Map<String, String>> changeAccess(@RequestBody AccessRequest accessRequest, Authentication authentication) {
        return userService.updateAccess(accessRequest,authentication);
    }
    @GetMapping("/api/auth/list")
    public ResponseEntity<List<UserResponseDTO>> getUserList() {
        return userService.getListOfUsers();
    }

    @DeleteMapping("/api/auth/user/{username}")
    public ResponseEntity<UserResponseDelete> deleteByUsername(@PathVariable String username) {
        return userService.deleteUser(username);
    }

}
