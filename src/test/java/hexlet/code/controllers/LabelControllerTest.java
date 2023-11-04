package hexlet.code.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import hexlet.code.utils.TestUtils;
import hexlet.code.dto.LabelDto;
import hexlet.code.repository.LabelRepository;
import hexlet.code.model.Label;
import org.assertj.core.api.Assertions;
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
import static hexlet.code.controllers.LabelController.LABEL_PATH;
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
public class LabelControllerTest {
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
    public void getLabelByIdTest() throws Exception {
        utils.regDefaultLabel();
        final Label expectedLabel = labelRepository.findAll().get(0);
        final var response = mockMvc.perform(
                        get(baseUrl + LABEL_PATH + "/{id}", expectedLabel.getId())
                                .header(AUTHORIZATION, utils.generateToken())
                ).andExpect(status().isOk())
                .andReturn()
                .getResponse();

        final Label label = fromJson(response.getContentAsString(), new TypeReference<>() {
        });

        assertEquals(expectedLabel.getId(), label.getId());
        assertEquals(expectedLabel.getName(), label.getName());
    }

    @Test
    public void getAllTest() throws Exception {
        utils.regDefaultLabel();
        final var response = mockMvc.perform(
                    get(baseUrl + LABEL_PATH)
                        .header(AUTHORIZATION, utils.generateToken())
                ).andExpect(status().isOk())
                .andReturn()
                .getResponse();

        final List<Label> labels = fromJson(response.getContentAsString(), new TypeReference<>() {
        });
        final List<Label> expected = labelRepository.findAll();
        Assertions.assertThat(labels)
                .containsAll(expected);
    }

    @Test
    public void getLabelsTest() throws Exception {
        utils.regDefaultLabel();
        final List<Label> expectedLabels = labelRepository.findAll();
        final var response = mockMvc.perform(
                        get(baseUrl + LABEL_PATH)
                                .header(AUTHORIZATION, utils.generateToken()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        final List<Label> labels = fromJson(response.getContentAsString(), new TypeReference<>() {
        });

        assertEquals(expectedLabels.size(), labels.size());
        assertEquals(expectedLabels.get(0).getId(), labels.get(0).getId());
        assertEquals(expectedLabels.get(1).getName(), labels.get(1).getName());
    }

    @Test
    public void createLabelTest() throws Exception {
        final var label = new LabelDto("Label 1");

//        final var request = buildRequestForSave(labelToSave);

        final var response = mockMvc.perform(post(baseUrl + LABEL_PATH)
                        .content(asJson(label))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(AUTHORIZATION, utils.generateToken())
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse();

        final Label savedLabel = fromJson(response.getContentAsString(), new TypeReference<>() {
        });
        final Label expectedLabel = labelRepository.findAll().get(0);
        assertEquals(labelRepository.findAll().size(), 1);
        assertEquals(expectedLabel.getName(), label.getName());
        assertThat(labelRepository.getReferenceById(savedLabel.getId())).isNotNull();
    }

    @Test
    public void updateLabelTest() throws Exception {
        utils.regDefaultLabel();
        final var expectedLabel = new LabelDto("Label 1");
        final long id = labelRepository.findAll().get(0).getId();
        final var response = mockMvc.perform(
                        put(baseUrl + LABEL_PATH + "/{id}", id)
                                .content(asJson(expectedLabel))
                                .contentType(MediaType.APPLICATION_JSON)
                                .header(AUTHORIZATION, utils.generateToken()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        final Label updatedLabel = labelRepository.findById(id);

        assertEquals(expectedLabel.getName(), updatedLabel.getName());
    }

    @Test
    public void deleteLabel() throws Exception {
        Label label1 = new Label();
        label1.setName("ASAP");
        labelRepository.save(label1);
        Label label2 = new Label();
        label2.setName("A$AP");
        labelRepository.save(label2);

        final long id = labelRepository.findAll().get(0).getId();
        final var response = mockMvc.perform(
                        delete(baseUrl + LABEL_PATH + "/{id}", id)
                                .header(AUTHORIZATION, utils.generateToken()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        assertFalse(labelRepository.existsById(id));
    }
}
