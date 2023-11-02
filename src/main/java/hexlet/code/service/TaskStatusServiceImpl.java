package hexlet.code.service;

import hexlet.code.dto.TaskStatusDto;
import hexlet.code.model.TaskStatus;
import hexlet.code.repository.TaskStatusRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class TaskStatusServiceImpl implements TaskStatusService {
    private final TaskStatusRepository statusRepository;

    @Override
    public TaskStatus getStatus(Long id) {
        return statusRepository.findById(id).orElseThrow();
    }

    @Override
    public List<TaskStatus> getStatuses() {
        return statusRepository.findAll();
    }

    @Override
    public TaskStatus createStatus(TaskStatusDto taskStatusDto) {
        TaskStatus taskStatus = new TaskStatus();
        taskStatus.setName(taskStatusDto.getName());
        return statusRepository.save(taskStatus);
    }

    @Override
    public TaskStatus updateStatus(TaskStatusDto taskStatusDto, Long id) {
        TaskStatus taskStatus = statusRepository.getReferenceById(id);
        taskStatus.setName(taskStatusDto.getName());
        return statusRepository.save(taskStatus);
    }

    @Override
    public void deleteStatus(Long id) {
        statusRepository.deleteById(id);
    }
}
