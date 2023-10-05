package hexlet.code.controllers;

import hexlet.code.dto.LabelDto;
import hexlet.code.model.Label;
import hexlet.code.service.LabelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static hexlet.code.controllers.LabelController.LABEL_PATH;
import static org.springframework.http.HttpStatus.CREATED;

@AllArgsConstructor
@RestController
@RequestMapping("${base-url}" + LABEL_PATH)
public class LabelController {
    public static final String LABEL_PATH = "/labels";
    public static final String ID = "/{id}";
    @Autowired
    private final LabelService labelService;

    @Operation(summary = "Create new label")
    @ApiResponse(responseCode = "201", description = "Label created")
    @PostMapping
    @ResponseStatus(CREATED)
    public Label createNew(@RequestBody @Valid final LabelDto labelDto) {
        return labelService.createNewLabel(labelDto);
    }

    @Operation(summary = "Get all labels")
    @ApiResponses(@ApiResponse(responseCode = "200", content =
        @Content(schema = @Schema(implementation = Label.class))
        ))
    @GetMapping
    public List<Label> getAll() {
        return labelService.getAll();
    }

    @Operation(summary = "Find label by Id")
    @ApiResponses(@ApiResponse(responseCode = "200"))
    @GetMapping(ID)
    public Label getLabelById(@PathVariable final Long id) {
        return labelService.getLabelById(id);
    }

    @Operation(summary = "Update a label")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Label updated",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Label.class))}),
        @ApiResponse(responseCode = "404", description = "Label with that id not found")
    })
    @PutMapping(ID)
    public Label update(@PathVariable final long id, @RequestBody final LabelDto labelDto) {
        return labelService.updateLabel(id, labelDto);
    }

    @Operation(summary = "Delete a label")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Label deleted"),
        @ApiResponse(responseCode = "404", description = "Label with that id not found")
    })
    @DeleteMapping(ID)
    public void delete(@PathVariable final long id) {
        labelService.deletelabel(id);
    }

}
