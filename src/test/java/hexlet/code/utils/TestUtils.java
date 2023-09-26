package hexlet.code.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.component.JWTHelper;
import hexlet.code.repository.LabelRepository;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.repository.TaskRepository;
import hexlet.code.repository.UserRepository;
import hexlet.code.model.Label;
import hexlet.code.model.TaskStatus;
import hexlet.code.model.Task;
import hexlet.code.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Map;


@Component
public class TestUtils {

    public static final String TEST_EMAIL = "email@email.com";
    public static final String TEST_EMAIL_2 = "email2@email.com";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskStatusRepository statusRepository;

    @Autowired
    private LabelRepository labelRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private JWTHelper jwtHelper;

    @TestConfiguration
    @ActiveProfiles("test")
    static class TestConfig {
    }

    public String generateToken() {
        return jwtHelper.expiring(Map.of("username",
                TEST_EMAIL, "password", "pwd123"));
    }

    public void regDefaultUsers() {
        userRepository.save(User.builder()
                .email(TEST_EMAIL)
                .firstName("fname")
                .lastName("lname")
                .password("pwd123")
                .build());

        userRepository.save(User.builder()
                .email(TEST_EMAIL_2)
                .firstName("fname2")
                .lastName("lname2")
                .password("pwd1234")
                .build());
    }

    public void regDefaultLabel() {
        Label label1 = new Label();
        label1.setName("ASAP");
        Label label2 = new Label();
        label2.setName("A$AP");
        regDefaultTask();
        labelRepository.save(label1);
        labelRepository.save(label2);
        taskRepository.findAll().get(0).setLabels(List.of(label1, label2));
    }

    public void regDefaultTask() {
        regDefaultUsers();
        regDefaultStatus();
        User author = userRepository.findAll().get(0);
        User executor = userRepository.findAll().get(1);
        TaskStatus status = statusRepository.findAll().get(0);
        taskRepository.save(Task.builder()
                .name("Task name")
                .author(author)
                .executor(executor)
                .taskStatus(status)
                .description("Some Description")
                .build());

        taskRepository.save(Task.builder()
                .name("Task name 2")
                .author(author)
                .executor(executor)
                .taskStatus(status)
                .description("Some Description 2")
                .build());

    }

    public void regDefaultStatus() {
        statusRepository.save(TaskStatus.builder()
                .name("To Do")
                .build());
        statusRepository.save(TaskStatus.builder()
                .name("Ready")
                .build());
    }


    private static final ObjectMapper MAPPER = new ObjectMapper().findAndRegisterModules();

    public static String asJson(final Object object) throws JsonProcessingException {
        return MAPPER.writeValueAsString(object);
    }

    public static <T> T fromJson(final String json, final TypeReference<T> to) throws JsonProcessingException {
        return MAPPER.readValue(json, to);
    }
}
