package hexlet.code.service;

import hexlet.code.dto.UserDto;
import hexlet.code.model.User;

import java.util.List;

public interface UserService {
    User createUser(UserDto userDto) throws Exception;
    User updateUser(long id, UserDto userDto) throws Exception;
    User getUserById(Long id);

    List<User> getUsers();

    void deleteUser(Long id);

    String getCurrentUserName();

    User getCurrentUser();

    User findUserByEmail(String email);
}
