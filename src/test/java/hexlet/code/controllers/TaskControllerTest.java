package hexlet.code.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import hexlet.code.utils.TestUtils;
import hexlet.code.dto.TaskDto;
import hexlet.code.repository.LabelRepository;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.repository.TaskRepository;
import hexlet.code.repository.UserRepository;
import hexlet.code.model.Task;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static hexlet.code.utils.TestUtils.asJson;
import static hexlet.code.utils.TestUtils.fromJson;
import static hexlet.code.controllers.TaskController.TASK_PATH;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
//@ActiveProfiles(TEST_PROFILE)
//@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
//@SpringBootTest(webEnvironment = RANDOM_PORT, classes = SpringConfigTests.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
@Transactional
public class TaskControllerTest {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskStatusRepository statusRepository;

    @Autowired
    private LabelRepository labelRepository;

    @Value("${base-url}")
    @Autowired
    private String baseUrl;

    @Autowired
    private TestUtils utils;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void getTaskByIdTest() throws Exception {
        utils.regDefaultTask();
        final Task expectedTask = taskRepository.findAll().get(0);
        final var response = mockMvc.perform(
                        get(baseUrl + TASK_PATH + "/{id}", expectedTask.getId())
                                .header(AUTHORIZATION, utils.generateToken())
                ).andExpect(status().isOk())
                .andReturn()
                .getResponse();

        final Task task = fromJson(response.getContentAsString(), new TypeReference<>() {
        });

        assertEquals(expectedTask.getId(), task.getId());
        assertEquals(expectedTask.getTaskStatus().getName(), task.getTaskStatus().getName());
        assertEquals(expectedTask.getName(), task.getName());
        assertEquals(expectedTask.getAuthor().getFirstName(), task.getAuthor().getFirstName());
    }

    @Test
    public void getTasksTest() throws Exception {
        utils.regDefaultTask();
        final List<Task> expectedTasks = taskRepository.findAll();
        final var response = mockMvc.perform(
                        get(baseUrl + TASK_PATH)
                                .header(AUTHORIZATION, utils.generateToken()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        final List<Task> tasks = fromJson(response.getContentAsString(), new TypeReference<>() {
        });

        assertEquals(expectedTasks.size(), tasks.size());
        assertEquals(expectedTasks.get(0).getTaskStatus().getName(), tasks.get(0).getTaskStatus().getName());
        assertEquals(expectedTasks.get(1).getAuthor().getFirstName(), tasks.get(1).getAuthor().getFirstName());
        assertEquals(expectedTasks.get(1).getDescription(), tasks.get(1).getDescription());
    }


    @Test
    public void createTaskTest() throws Exception {
        utils.regDefaultTask();
        final var task = TaskDto.builder()
                .name("Task ")
                .description("Description 2")
                .taskStatusId(taskRepository.findAll().get(0).getTaskStatus().getId())
                .build();
        mockMvc.perform(
                        post(baseUrl + TASK_PATH)
                                .content(asJson(task))
                                .contentType(MediaType.APPLICATION_JSON)
                                .header(AUTHORIZATION, utils.generateToken()))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse();

        assertEquals(taskRepository.findAll().size(), 3);
        assertEquals(taskRepository.findAll().get(2).getDescription(), "Description 2");
    }

    @Test
    public void updateTaskTest() throws Exception {
        String sql = "ALTER TABLE statuses ALTER COLUMN id RESTART WITH 1";
        jdbcTemplate.execute(sql);
        utils.regDefaultTask();


        final var expectedTask = TaskDto.builder()
                .name("Task 1")
                .description("Desc 1")
                .taskStatusId(1L)
                .build();
        mockMvc.perform(
                        put(baseUrl + TASK_PATH + "/{id}", taskRepository.findAll().get(0).getId())
                                .content(asJson(expectedTask))
                                .contentType(MediaType.APPLICATION_JSON)
                                .header(AUTHORIZATION, utils.generateToken()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        assertEquals(expectedTask.getName(), taskRepository.findAll().get(0).getName());
        assertEquals(expectedTask.getDescription(), taskRepository.findAll().get(0).getDescription());
    }

    @Test
    public void deleteTask() throws Exception {
        utils.regDefaultTask();

        final var response = mockMvc.perform(
                        delete(baseUrl + TASK_PATH + "/{id}", taskRepository.findAll().get(0).getId())
                                .header(AUTHORIZATION, utils.generateToken()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        assertEquals(taskRepository.findAll().size(), 1);
    }
}
