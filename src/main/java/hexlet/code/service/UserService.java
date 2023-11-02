package hexlet.code.service;

import hexlet.code.dto.UserDto;
import hexlet.code.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    User createUser(UserDto userDto);
    User updateUser(long id, UserDto userDto);
    User getUserById(Long id);

    List<User> getUsers();

    void deleteUser(Long id);

    String getCurrentUserName();

    User getCurrentUser();

    Optional<User> findUserByEmail(String email);
}
