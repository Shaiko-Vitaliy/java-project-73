package hexlet.code.service;

import hexlet.code.dto.UserDto;
import hexlet.code.exception.DataException;
import hexlet.code.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    User createUser(UserDto userDto) throws Exception;
    User updateUser(long id, UserDto userDto) throws Exception;
    User getUserById(Long id);

    List<User> getUsers();

    void deleteUser(Long id) throws DataException;

    String getCurrentUserName();

    User getCurrentUser();

    Optional<User> findUserByEmail(String email);
}
