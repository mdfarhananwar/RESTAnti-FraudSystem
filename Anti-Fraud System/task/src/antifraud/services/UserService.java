package antifraud.services;

import antifraud.dao.userDao.RoleRepository;
import antifraud.dao.userDao.UserRepository;
import antifraud.models.userModel.User;
import antifraud.models.userModel.UserList;
import antifraud.models.userModel.UserResponseDTO;
import antifraud.models.userModel.UserResponseDelete;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    public ResponseEntity<List<UserResponseDTO>> getListOfUsers() {
        System.out.println();
        List<User> all = userRepository.findAll();
        List<UserResponseDTO> userResponses = all.stream()
                .map(UserResponseDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(userResponses);
    }

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findUserByUsername(String username) {
        System.out.println(username + " username");
        Optional<User> optionalUser = userRepository.findByUsername(username);
        System.out.println("FindUSERBYUSERNAME");
        System.out.println(optionalUser);
        System.out.println("FindUSERBYUSERNAME");
        return optionalUser.orElse(null);
    }

    @Transactional
    public User saveUser(User user) {
        System.out.println("USERfrom User");
        System.out.println(user.toString());

        User savedUser = userRepository.save(user);
        System.out.println("USER AFTER ASSIGNING ROLE");
        System.out.println(savedUser.toString());
        return savedUser;
    }

    public UserResponseDTO mapToUserResponseDTO(User user) {
        return new UserResponseDTO(user);
    }
    public UserList mapToUserList(User user) {
        UserList userList = new UserList();
        userList.setName(user.getName());
        userList.setUsername(user.getUsername());
        return userList;
    }
    public UserResponseDelete deleteByUsername(String username) {
            User deleteUser = findUserByUsername(username);
            userRepository.delete(deleteUser);
            UserResponseDelete userResponseDelete = new UserResponseDelete();
            userResponseDelete.setUsername(username);
            userResponseDelete.setStatus("Deleted successfully!");
            return userResponseDelete;
    }
    public Long count() {
        return userRepository.count();
    }
    public boolean existsByUsername (String username) {
        return userRepository.existsByUsernameIgnoreCase(username);
    }

@Transactional
public ResponseEntity<UserResponseDelete> deleteUser(String username) {
//    System.out.println(!userRepository.existsByUsernameIgnoreCase(username));
    if (!userRepository.existsByUsernameIgnoreCase(username)) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
//            System.out.println("checkUSer");
            User checkUser = findUserByUsername(username);
//        if (checkUser == null) {
//            System.out.println("aaaaaaaaaaaaaaaaaaaa");
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
//        }
        String roleName = checkUser.getRole().getName();

    if (roleName.equals("ADMINISTRATOR")) {
        System.out.println("An admin cannot delete oneself.");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    userRepository.deleteByUsernameIgnoreCase(username);

    return ResponseEntity.ok(new UserResponseDelete(username,"Deleted successfully!" ));
}

}
