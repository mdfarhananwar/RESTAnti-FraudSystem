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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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

    public ResponseEntity<UserResponseDTO> registerUser(User user) {
        User checkUser = findUserByUsername(user.getUsername());
        if (user.getUsername() == null || user.getName() == null || user.getPassword() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } else if (checkUser != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } else {
            user.setPassword(passwordEncoder().encode(user.getPassword()));
            String roleName;
            Operation operation;
            if (count() == 0) {
                roleName = "ADMINISTRATOR";
                operation = Operation.UNLOCK;
            } else {
                roleName = "MERCHANT";
                operation = Operation.LOCK;
            }
            Role role = new Role(roleName);
            roleService.saveRole(role);
            user.setRole(role);
            user.setOperation(operation);
            User savedUser = saveUser(user);
            System.out.println(savedUser.toString());
            UserResponseDTO userResponse = mapToUserResponseDTO(savedUser);
            return ResponseEntity.status(HttpStatus.CREATED).body(userResponse);
        }
    }

    public ResponseEntity<List<UserResponseDTO>> getListOfUsers() {
        List<User> all = userRepository.findAll();
        List<UserResponseDTO> userResponses = all.stream()
                .map(UserResponseDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(userResponses);
    }

    public ResponseEntity<UserResponseDTO> updateRole(UserRole userRole) {
        User checkUser;

        if (!existsByUsername(userRole.getUsername())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } else {
            checkUser = findUserByUsername(userRole.getUsername());
        }

        if (!userRole.getRole().equals("SUPPORT") && !userRole.getRole().equals("MERCHANT")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } else if (userRole.getRole().equals(checkUser.getRole().getName())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } else {
            Role role = new Role(userRole.getRole());
            roleService.saveRole(role);
            checkUser.setRole(role);
            saveUser(checkUser);
            checkUser.getRole().setName(userRole.getRole());
            return ResponseEntity.status(HttpStatus.OK).body(new UserResponseDTO(checkUser));
        }
    }

    public ResponseEntity<Map<String, String>> updateAccess(AccessRequest accessRequest, Authentication authentication) {
        String username = accessRequest.getUsername();
        Operation operation = accessRequest.getOperation();
        User checkedUser = findUserByUsername(username);
        String authenticatedUsername = authentication.getName();
        User authenticatedUser = findUserByUsername(authenticatedUsername);
        String authenticatedRoleName = authenticatedUser.getRole().getName();
        if (checkedUser == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        String roleName = checkedUser.getRole().getName();
        if (roleName.equals("ADMINISTRATOR") && operation == Operation.LOCK) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        if (operation == Operation.UNLOCK && !authenticatedRoleName.equals("ADMINISTRATOR")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        String status = operation == Operation.LOCK ? "locked" : "unlocked";
        checkedUser.setOperation(operation);
        saveUser(checkedUser);
        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("status", "User " + username + " " + status + "!");
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }

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

    @Transactional
    public ResponseEntity<UserResponseDelete> deleteUser(String username) {
        if (!userRepository.existsByUsernameIgnoreCase(username)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        User checkUser = findUserByUsername(username);
        String roleName = checkUser.getRole().getName();

        if (roleName.equals("ADMINISTRATOR")) {
            System.out.println("An admin cannot delete oneself.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        userRepository.deleteByUsernameIgnoreCase(username);
        return ResponseEntity.ok(new UserResponseDelete(username, "Deleted successfully!"));
    }

    public boolean existsByUsername(String name) {
        return userRepository.existsByUsernameIgnoreCase(name);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
