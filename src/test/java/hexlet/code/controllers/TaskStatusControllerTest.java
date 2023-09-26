package hexlet.code.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import hexlet.code.utils.TestUtils;
import hexlet.code.dto.TaskStatusDto;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.model.TaskStatus;
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
import static hexlet.code.controllers.TaskStatusController.TASK_STATUS_PATH;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
//@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
//@SpringBootTest(webEnvironment = RANDOM_PORT, classes = SpringConfigTests.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
@Transactional
public class TaskStatusControllerTest {

    @Autowired
    private TaskStatusRepository statusRepository;

    @Value("${base-url}")
    @Autowired
    private String baseUrl;

    @Autowired
    private TestUtils utils;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void getStatusByIdTest() throws Exception {
        utils.regDefaultStatus();
        TaskStatus expectedStatus = statusRepository.findAll().get(0);

        final var response = mockMvc.perform(
                        get(baseUrl + TASK_STATUS_PATH + "/{id}", expectedStatus.getId())
                                .header(AUTHORIZATION, utils.generateToken())
                ).andExpect(status().isOk())
                .andReturn()
                .getResponse();

        final TaskStatus actual = fromJson(response.getContentAsString(), new TypeReference<>() {
        });

        assertEquals(expectedStatus.getId(), actual.getId());
        assertEquals(expectedStatus.getName(), actual.getName());
    }

    @Test
    public void getStatuses() throws Exception {
        utils.regDefaultStatus();
        final List<TaskStatus> expected = statusRepository.findAll();
        final var response = mockMvc.perform(
                        get(baseUrl + TASK_STATUS_PATH)
                                .header(AUTHORIZATION, utils.generateToken())
                ).andExpect(status().isOk())
                .andReturn()
                .getResponse();

        final List<TaskStatus> actual = fromJson(response.getContentAsString(), new TypeReference<>() {
        });
        assertEquals(expected.size(), actual.size());
        assertEquals(expected.get(0).getId(), actual.get(0).getId());
        assertEquals(expected.get(1).getName(), actual.get(1).getName());
    }

    @Test
    public void createStatusTest() throws Exception {
        utils.regDefaultUsers();
        TaskStatusDto status = TaskStatusDto.builder().name("To do").build();

        mockMvc.perform(
                        post(baseUrl + TASK_STATUS_PATH)
                                .content(asJson(status))
                                .contentType(MediaType.APPLICATION_JSON)
                                .header(AUTHORIZATION, utils.generateToken()))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse();

        assertEquals(statusRepository.findAll().size(), 1);
        assertEquals(statusRepository.findAll().get(0).getName(), "To do");
    }

    @Test
    public void updateStatusTest() throws Exception {
        utils.regDefaultStatus();


        TaskStatusDto status = new TaskStatusDto("ToDo2");

        final var response = mockMvc.perform(
                        put(baseUrl + TASK_STATUS_PATH + "/{id}", statusRepository.findAll().get(0).getId())
                                .content(asJson(status))
                                .contentType(MediaType.APPLICATION_JSON)
                                .header(AUTHORIZATION, utils.generateToken()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        assertEquals(status.getName(), statusRepository.findAll().get(0).getName());
    }

    @Test
    public void deleteStatusTest() throws Exception {
        utils.regDefaultStatus();

        final var response = mockMvc.perform(
                        delete(baseUrl + TASK_STATUS_PATH + "/{id}", statusRepository.findAll().get(0).getId())
                                .header(AUTHORIZATION, utils.generateToken()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        assertEquals(statusRepository.findAll().size(), 1);
    }
}
