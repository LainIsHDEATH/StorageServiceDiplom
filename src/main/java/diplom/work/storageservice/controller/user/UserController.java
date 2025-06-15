package diplom.work.storageservice.controller.user;

import diplom.work.storageservice.dto.user.*;
import diplom.work.storageservice.model.user.User;
import diplom.work.storageservice.service.user.UserService;
import diplom.work.storageservice.util.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserMapper userMapper;
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserRegisterDTO userDTO) {
        User user = userMapper.toEntity(userDTO);

        return ResponseEntity.status(201).body(
                userMapper.toDto(
                        userService.registerUser(user)));
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponseDTO> loginUser(@RequestBody UserAuthDTO userDTO) {
        User user = userService.loginUser(userDTO.email(), userDTO.password());
        return ResponseEntity.ok(userMapper.toDto(user));
    }


    @GetMapping("/email/{email}")
    public ResponseEntity<UserResponseDTO> getUserByEmail(@PathVariable String email) {
        return userService.findByEmail(email)
                .map(userMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<UserResponseDTO> getUserByUsername(@PathVariable String username) {
        return userService.findByUsername(username)
                .map(userMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping()
    public ResponseEntity<UserListResponseDTO> getAllUsers() {
        return ResponseEntity.status(200).body(
                new UserListResponseDTO(
                        userService.findAllUsers()
                                .stream()
                                .map(userMapper::toDto)
                                .toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long id) {
        return userService.findUserById(id)
                .map(userMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id,
                                        @RequestBody UserUpdateDTO userDTO) {
        User userDetails = userMapper.toEntity(userDTO);

        try {
            return ResponseEntity.ok(
                    userMapper.toDto(
                            userService.updateUser(id, userDetails)));
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }
}
