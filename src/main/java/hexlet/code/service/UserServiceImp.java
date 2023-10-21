package hexlet.code.service;

import hexlet.code.dto.UserDto;
import hexlet.code.exception.DataException;
import hexlet.code.model.Task;
import hexlet.code.model.User;
import hexlet.code.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@AllArgsConstructor
public class UserServiceImp implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private final PasswordEncoder passwordEncoder;

    @Override
    public User createUser(UserDto userDto) throws Exception {
        final Optional<User> existentUser = findUserByEmail(userDto.getEmail());
        if (existentUser.isPresent()) {
            throw new DataException("The User exist");
        }
        final User user = new User();
        user.setEmail(userDto.getEmail());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public User updateUser(final long id, final UserDto userDto) throws Exception {
        final Optional<User> existentUser = findUserByEmail(userDto.getEmail());
        if (existentUser.isPresent()) {
            throw new DataException("The User exist");
        }
        final User userToUpdate = userRepository.findById(id).get();
        userToUpdate.setEmail(userDto.getEmail());
        userToUpdate.setFirstName(userDto.getFirstName());
        userToUpdate.setLastName(userDto.getLastName());
        userToUpdate.setPassword(passwordEncoder.encode(userDto.getPassword()));
        return userRepository.save(userToUpdate);
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow();
    }

    @Override
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @Override
    public void deleteUser(Long id) throws DataException {
        User user = userRepository.findById(id).orElseThrow();
        List<Task> listOfTaskAsAuthor = user.getTasksAsAuthor();
        List<Task> listOfTaskAsExecutor = user.getTasksAsExecutor();
        if (listOfTaskAsExecutor == null || listOfTaskAsAuthor == null) {
            userRepository.deleteById(id);
            return;
        }
        if (!listOfTaskAsAuthor.isEmpty() || !listOfTaskAsExecutor.isEmpty()) {
            throw new DataException("The User is associated with the task! Cannot be deleted! ");
        }
        userRepository.deleteById(id);
    }

    public String getCurrentUserName() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    public User getCurrentUser() {
        return userRepository.findByEmail(getCurrentUserName()).get();
    }

    @Override
    public Optional<User> findUserByEmail(String email) {
        return userRepository.findUserByEmailIgnoreCase(email);
    }
}
