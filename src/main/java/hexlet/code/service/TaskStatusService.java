package hexlet.code.service;

import hexlet.code.exception.DataException;
import hexlet.code.model.TaskStatus;
import hexlet.code.dto.TaskStatusDto;

import java.util.List;

public interface TaskStatusService {
    TaskStatus getStatus(Long id);

    List<TaskStatus> getStatuses();

    TaskStatus createStatus(TaskStatusDto taskStatusDto);

    TaskStatus updateStatus(TaskStatusDto taskStatusDto, Long id);

    void deleteStatus(Long id) throws DataException;
}
