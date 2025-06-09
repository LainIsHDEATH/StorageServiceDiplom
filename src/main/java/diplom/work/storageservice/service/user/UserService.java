package diplom.work.storageservice.service.user;

import diplom.work.storageservice.model.user.User;
import diplom.work.storageservice.repository.user.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> findUserById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Transactional
    public User registerUser(User user) {
        return userRepository.save(user);
    }

    @Transactional
    public User updateUser(Long id, User updatedUser) {
        return userRepository.findById(id)
                .map(user -> {
                    Optional.ofNullable(updatedUser.getUsername()).ifPresent(user::setUsername);
                    Optional.ofNullable(updatedUser.getPassword()).ifPresent(user::setPassword);
                    Optional.ofNullable(updatedUser.getEmail()).ifPresent(user::setEmail);
                    return userRepository.save(user);
                })
                .orElseThrow(() -> new EntityNotFoundException("User with ID " + id + " not found"));
    }

    @Transactional
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }
}
