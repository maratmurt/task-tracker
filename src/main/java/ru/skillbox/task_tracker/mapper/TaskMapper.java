package ru.skillbox.task_tracker.mapper;

import org.mapstruct.Mapper;
import ru.skillbox.task_tracker.dto.task.TaskRequest;
import ru.skillbox.task_tracker.dto.task.TaskResponse;
import ru.skillbox.task_tracker.model.Task;

import java.time.Instant;

@Mapper(componentModel = "spring")
public interface TaskMapper {

    Task toTask(TaskRequest taskRequest);

    TaskResponse toResponse(Task task);

    default Long map(Instant instant) {
        return instant == null ? null : instant.toEpochMilli();
    }

}
