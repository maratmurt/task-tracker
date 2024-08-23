package ru.skillbox.task_tracker.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.skillbox.task_tracker.mapper.NullAwareMapper;
import ru.skillbox.task_tracker.model.Task;
import ru.skillbox.task_tracker.model.User;
import ru.skillbox.task_tracker.repository.TaskRepository;
import ru.skillbox.task_tracker.repository.UserRepository;

import java.lang.reflect.InvocationTargetException;
import java.time.Instant;
import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;

    private final UserRepository userRepository;
    
    private final NullAwareMapper nullAwareMapper;

    public Flux<Task> findAll() {
        return taskRepository.findAll()
                .flatMap(task -> findById(task.getId()));
    }

    public Mono<Task> findById(String id) {
        Mono<Task> taskMono = taskRepository.findById(id);
        Mono<User> authorMono = taskMono.flatMap(
                task -> userRepository.findById(task.getAuthorId()));
        Mono<User> assigneeMono = taskMono.flatMap(
                task -> userRepository.findById(task.getAssigneeId()));
        Mono<List<User>> observersMono = taskMono.flatMap(
                task -> userRepository.findAllById(task.getObserverIds()).collectList());

        return Mono.zip(taskMono, authorMono, assigneeMono, observersMono)
                .map(tuple -> {
                    Task task = tuple.getT1();
                    task.setAuthor(tuple.getT2());
                    task.setAssignee(tuple.getT3());
                    task.setObservers(new HashSet<>(tuple.getT4()));
                    return task;
                });
    }

    public Mono<Task> create(Task task) {
        task.setCreatedAt(Instant.now());
        task.setUpdatedAt(Instant.now());

        Mono<User> authorMono = userRepository.findById(task.getAuthorId());
        Mono<User> assigneeMono = userRepository.findById(task.getAssigneeId());
        Mono<List<User>> observers = userRepository.findAllById(task.getObserverIds()).collectList();

        return Mono.zip(authorMono, assigneeMono, observers)
                .map(tuple -> {
                    task.setAuthor(tuple.getT1());
                    task.setAssignee(tuple.getT2());
                    task.setObservers(new HashSet<>(tuple.getT3()));
                    return task;
                })
                .flatMap(taskRepository::save);
    }

    public Mono<Task> update(String id, Task updatedTask) {
        return taskRepository.findById(id)
                .map(existingTask -> {
                    try {
                        nullAwareMapper.copyProperties(existingTask, updatedTask);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        throw new RuntimeException(e);
                    }
                    existingTask.setUpdatedAt(Instant.now());
                    return existingTask;
                })
                .flatMap(taskRepository::save)
                .flatMap(task -> findById(id));
    }

    public Mono<Void> delete(String id) {
        return taskRepository.deleteById(id);
    }

    public Mono<Task> addObserver(String taskId, String userId) {
        Mono<Task> taskMono = taskRepository.findById(taskId);
        Mono<User> observerMono = userRepository.findById(userId);

        return Mono.zip(taskMono, observerMono)
                .map(tuple -> {
                    Task task = tuple.getT1();
                    User observerToAdd = tuple.getT2();
                    task.getObserverIds().add(observerToAdd.getId());
                    return task;
                })
                .flatMap(taskRepository::save)
                .flatMap(task -> findById(taskId));
    }
}
