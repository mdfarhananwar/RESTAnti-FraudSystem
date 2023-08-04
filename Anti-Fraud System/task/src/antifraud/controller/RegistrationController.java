package antifraud.controller;

import antifraud.models.userModel.*;
import antifraud.services.RoleService;
import antifraud.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class RegistrationController {

    private final RoleService roleService;
    private final UserService userService;

    @Autowired
    RegistrationController(RoleService roleService, UserService userService) {
        this.roleService = roleService;
        this.userService = userService;
    }

    @PostMapping("/api/auth/user")
    public ResponseEntity<UserResponseDTO> register(@RequestBody User user) {

        System.out.println("Enter");
        System.out.println(user.getUsername());
        User checkUser = userService.findUserByUsername(user.getUsername());
        if (user.getUsername() == null || user.getName() == null || user.getPassword() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } else if (checkUser != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } else {
            user.setPassword(passwordEncoder().encode(user.getPassword()));

            //ole savedRole = roleService.saveRole(user);

            String roleName;
            Operation operation;
            if (userService.count() == 0) {
                //role.setName("ADMINISTRATOR");
                roleName = "ADMINISTRATOR";
                operation = Operation.UNLOCK;
                //user.getRole().setName("ADMINISTRATOR");
            } else {
                //user.getRole().setName("MERCHANT");
                //role.setName("MERCHANT");
                roleName = "MERCHANT";
                operation = Operation.LOCK;
            }
            Role role = new Role(roleName);
            roleService.saveRole(role);
            user.setRole(role);
            user.setOperation(operation);
            User savedUser = userService.saveUser(user);
            System.out.println(savedUser.toString());
            UserResponseDTO userResponse = userService.mapToUserResponseDTO(savedUser);
            return ResponseEntity.status(HttpStatus.CREATED).body(userResponse);
        }
    }
    @PutMapping("/api/auth/role")
    public ResponseEntity<UserResponseDTO> changeRole(@RequestBody UserRole userRole) {

        User checkUser = userService.findUserByUsername(userRole.getUsername());
        System.out.println(userRole);
        //checkUser.getRole().setName(userRole.getRole());
        //System.out.println(checkUser.toString());
        if (checkUser == null) {
            System.out.println("aaaaaaaaaaaaaaaaaaaa");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        } else if (!userRole.getRole().equals("SUPPORT") && !userRole.getRole().equals("MERCHANT")) {
            System.out.println("bbbbbbbbbbbbbbbbb");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            //return ResponseEntity.status(HttpStatus.OK).body(new UserResponseDTO(checkUser));
        } else if (userRole.getRole().equals(checkUser.getRole().getName())) {
            System.out.println(userRole.getRole());
            System.out.println("ccccccccccccccccccccc");
            System.out.println(checkUser.getRole().getName());
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } else {
            System.out.println("ddddddddddddddddddd");
            // Update the user's role
            Role role = new Role(userRole.getRole());
            roleService.saveRole(role);
            checkUser.setRole(role);

            // Save the updated user to the database
            userService.saveUser(checkUser);
            checkUser.getRole().setName(userRole.getRole());
            return ResponseEntity.status(HttpStatus.OK).body(new UserResponseDTO(checkUser));
        }
    }
    @PutMapping("/api/auth/access")
    public ResponseEntity<Map<String, String>> changeAccess(@RequestBody AccessRequest accessRequest, Authentication authentication) {
        System.out.println("Enter");
        String username = accessRequest.getUsername();
        Operation operation = accessRequest.getOperation();
        System.out.println(username);
        User checkedUser = userService.findUserByUsername(username);
        System.out.println("CHECKING ACESS CHECK USER");
        System.out.println(checkedUser.toString());
        String authenticatedUsername = authentication.getName();
        User authenticatedUser = userService.findUserByUsername(authenticatedUsername);
        String authenticatedRoleName = authenticatedUser.getRole().getName();
        System.out.println("Authenticated User: " + authenticatedUser.toString());
        if (checkedUser == null) {
            System.out.println("NULL");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        String roleName = checkedUser.getRole().getName();
        if (roleName.equals("ADMINISTRATOR") && operation == Operation.LOCK) {
            System.out.println("ADMINLOCK");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        if (operation == Operation.UNLOCK && !authenticatedRoleName.equals("ADMINISTRATOR")) {
            System.out.println("ADMINNOT");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        System.out.println("In CReation Access");
        String status = operation == Operation.LOCK ? "locked" : "unlocked";
        checkedUser.setOperation(operation);
        userService.saveUser(checkedUser);
        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("status", "User " + username + " " + status + "!");
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }
    @GetMapping("/api/auth/list")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'SUPPORT')")
    public ResponseEntity<List<UserResponseDTO>> getUserList() {
        return userService.getListOfUsers();
    }
    @PreAuthorize("ADMINISTRATOR")
    @DeleteMapping("/api/auth/user/{username}")
    public ResponseEntity<UserResponseDelete> deleteByUsername(@PathVariable String username) {
//        return userService.deleteByUsername(username);
//        System.out.println(userService.existsByUsername(username));
//        if (!userService.existsByUsername(username)) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
//        }
        System.out.println(username);
//        User checkUser = userService.findUserByUsername(username);
//        System.out.println("checkUSer");
//        //checkUser.getRole().setName(userRole.getRole());
//        //System.out.println(checkUser.toString());
//        if (checkUser == null) {
//            System.out.println("aaaaaaaaaaaaaaaaaaaa");
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
//        }
        System.out.println("we are here");
        return userService.deleteUser(username);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
