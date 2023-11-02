package hexlet.code.controllers;


import hexlet.code.dto.TaskStatusDto;
import hexlet.code.model.TaskStatus;
import hexlet.code.service.TaskStatusService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
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

import static hexlet.code.controllers.TaskStatusController.TASK_STATUS_PATH;

@RestController
@RequestMapping("${base-url}" + TASK_STATUS_PATH)
@AllArgsConstructor
public class TaskStatusController {
    private final TaskStatusService statusService;
    public static final String TASK_STATUS_PATH = "/statuses";

    @GetMapping("/{id}")
    public TaskStatus getStatus(@PathVariable("id") Long id) {
        return statusService.getStatus(id);
    }

    @GetMapping("")
    public List<TaskStatus> getStatuses() {
        return statusService.getStatuses();
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public TaskStatus createStatus(@RequestBody @Valid TaskStatusDto taskStatusDto) {
        return statusService.createStatus(taskStatusDto);
    }

    @PutMapping("/{id}")
    public TaskStatus updateStatus(@RequestBody @Valid TaskStatusDto statusDto, @PathVariable Long id) {
        return statusService.updateStatus(statusDto, id);
    }

    @DeleteMapping("/{id}")
    public void deleteStatus(@PathVariable("id") Long id) {
        statusService.deleteStatus(id);
    }
}
