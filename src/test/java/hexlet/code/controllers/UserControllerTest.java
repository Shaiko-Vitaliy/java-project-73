package hexlet.code.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import hexlet.code.service.UserService;
import hexlet.code.utils.TestUtils;
import hexlet.code.model.User;
import hexlet.code.repository.UserRepository;
import hexlet.code.dto.UserDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static hexlet.code.utils.TestUtils.asJson;
import static hexlet.code.utils.TestUtils.fromJson;
import static hexlet.code.controllers.UserController.USER_CONTROLLER_PATH;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
@Transactional
public class UserControllerTest {

    private static final String TEST_EMAIL = "qwecvbngh65@ervbeb.ru";
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Value("${base-url}")
    @Autowired
    private String baseUrl;

    @Autowired
    private TestUtils utils;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void create() {
        utils.regDefaultUsers();
    }

    @Test
    public void getUserByIdTest() throws Exception {
        final User expectedUser = userRepository.findAll().get(0);
        final var response = mockMvc.perform(
                        get(baseUrl + USER_CONTROLLER_PATH + "/{id}", expectedUser.getId())
                                .header(AUTHORIZATION, utils.generateToken())
                ).andExpect(status().isOk())
                .andReturn()
                .getResponse();

        final User user = fromJson(response.getContentAsString(), new TypeReference<>() {
        });

        assertEquals(expectedUser.getId(), user.getId());
        assertEquals(expectedUser.getEmail(), user.getEmail());
        assertEquals(expectedUser.getFirstName(), user.getFirstName());
        assertEquals(expectedUser.getLastName(), user.getLastName());
    }

    @Test
    public void getUsersTest() throws Exception {
        final List<User> expectedUsers = userRepository.findAll();
        final var response = mockMvc.perform(
                        get(baseUrl + USER_CONTROLLER_PATH)
                                .header(AUTHORIZATION, utils.generateToken()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        final List<User> users = fromJson(response.getContentAsString(), new TypeReference<List<User>>() {
        });

        Assertions.assertThat(users)
                .containsAll(expectedUsers);
    }

    @Test
    public void createUserTest() throws Exception {
        userRepository.deleteAll();
        final var user = UserDto.builder()
                .email(TEST_EMAIL)
                .firstName("fname")
                .lastName("lname")
                .password("pwd123")
                .build();

        final var response = mockMvc.perform(
                        post(baseUrl + USER_CONTROLLER_PATH)
                                .content(asJson(user))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse();

        final var savedUser = fromJson(response.getContentAsString(), new TypeReference<User>() {
        });

        assertEquals(userRepository.findAll().size(), 1);
        assertEquals(savedUser.getEmail(), TEST_EMAIL);
        assertThat(userRepository.getReferenceById(savedUser.getId())).isNotNull();
    }

    @Test
    public void updateUserTest() throws Exception {
        final var expectedUser = UserDto.builder()
                .email(TEST_EMAIL)
                .firstName("fnameNew")
                .lastName("lnameNew")
                .password("pwd123New")
                .build();
        final long id = userRepository.findAll().get(0).getId();

        final var response = mockMvc.perform(
                        put(baseUrl + USER_CONTROLLER_PATH + "/{id}", id)
                                .content(asJson(expectedUser))
                                .contentType(MediaType.APPLICATION_JSON)
                                .header(AUTHORIZATION, utils.generateToken()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        final var savedUser = fromJson(response.getContentAsString(), new TypeReference<User>() {
        });

        assertEquals(expectedUser.getFirstName(), savedUser.getFirstName());
        assertEquals(expectedUser.getLastName(), savedUser.getLastName());
    }

    @Test
    public void deleteUser() throws Exception {
        final long id = userRepository.findAll().get(0).getId();
        final var response = mockMvc.perform(
                        delete(baseUrl + USER_CONTROLLER_PATH + "/{id}", id)
                                .header(AUTHORIZATION, utils.generateToken()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        assertFalse(userRepository.existsById(id));
    }
}
