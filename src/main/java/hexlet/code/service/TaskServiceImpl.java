package hexlet.code.service;

import com.querydsl.core.types.Predicate;
import hexlet.code.dto.TaskDto;
import hexlet.code.model.Label;
import hexlet.code.model.Task;
import hexlet.code.model.TaskStatus;
import hexlet.code.model.User;
import hexlet.code.repository.TaskRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@AllArgsConstructor
public class TaskServiceImpl implements TaskService {
    @Autowired
    private final TaskRepository taskRepository;
    @Autowired
    private final UserService userService;
    @Autowired
    private final TaskStatusService statusService;
    @Autowired
    private final LabelService labelService;

    @Override
    public Task createNewTask(TaskDto taskDto) {
        Task taskNew = new Task();
        Task taskResult = modificationTask(taskDto, taskNew);
        return taskRepository.save(taskResult);
    }

    @Override
    public Task updateTask(Long id, TaskDto taskDto) {
        Task task = taskRepository.getReferenceById(id);
        User author = userService.getCurrentUser();
        User executor = null;
        if (taskDto.getExecutorId() != null) {
            executor = userService.getUserById(taskDto.getExecutorId());
        }
        TaskStatus status = statusService.getStatus(taskDto.getTaskStatusId());
        List<Label> labels = new ArrayList<>();
        if (taskDto.getLabelIds() != null) {
            labels = labelService.getAllLabelById(taskDto.getLabelIds());
        }

        task.setName(taskDto.getName());
        task.setAuthor(author);
        task.setExecutor(executor);
        task.setDescription(taskDto.getDescription());
        task.setTaskStatus(status);
        task.setLabels(labels);
        return taskRepository.save(task);
    }

    @Override
    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }

    @Override
    public List<Task> getAll() {
        return taskRepository.findAll();
    }

    @Override
    public List<Task> getAll(Predicate predicate) {
        return StreamSupport.stream(taskRepository.findAll(predicate).spliterator(), false)
                .collect(Collectors.toList());
    }

    @Override
    public Task getTaskById(Long id) {
        return taskRepository.findById(id).orElseThrow();
    }

    private Task modificationTask(TaskDto taskDto, Task task) {
        User author = userService.getCurrentUser();
        User executor = null;
        if (taskDto.getExecutorId() != null) {
            executor = userService.getUserById(taskDto.getExecutorId());
        }
        TaskStatus status = statusService.getStatus(taskDto.getTaskStatusId());
        List<Label> labels = new ArrayList<>();
        if (taskDto.getLabelIds() != null) {
            labels = labelService.getAllLabelById(taskDto.getLabelIds());
        }

        task.setName(taskDto.getName());
        task.setAuthor(author);
        task.setExecutor(executor);
        task.setDescription(taskDto.getDescription());
        task.setTaskStatus(status);
        task.setLabels(labels);

        return task;
    }
}
