package ru.skillbox.task_tracker.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import ru.skillbox.task_tracker.model.Task;

public interface TaskRepository extends ReactiveMongoRepository<Task, String> {
}
