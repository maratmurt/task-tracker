package ru.skillbox.task_tracker.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import ru.skillbox.task_tracker.model.User;

public interface UserRepository extends ReactiveMongoRepository<User, String> {
}
