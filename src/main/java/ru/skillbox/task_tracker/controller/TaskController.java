package ru.skillbox.task_tracker.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.skillbox.task_tracker.dto.task.TaskRequest;
import ru.skillbox.task_tracker.dto.task.TaskResponse;
import ru.skillbox.task_tracker.mapper.TaskMapper;
import ru.skillbox.task_tracker.service.TaskService;

@RestController
@RequestMapping("/api/task")
@RequiredArgsConstructor
@Slf4j
public class TaskController {

    private final TaskService taskService;

    private final TaskMapper taskMapper;

    @GetMapping
    public Flux<TaskResponse> findAll() {
        return taskService.findAll()
                .map(taskMapper::toResponse);
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<TaskResponse>> findById(@PathVariable String id) {
        return taskService.findById(id)
                .map(taskMapper::toResponse)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Mono<ResponseEntity<TaskResponse>> create(@RequestBody TaskRequest request) {
        return taskService.create(taskMapper.toTask(request))
                .map(taskMapper::toResponse)
                .map(ResponseEntity::ok);
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<TaskResponse>> update(@PathVariable String id, @RequestBody TaskRequest request) {
        return taskService.update(id, taskMapper.toTask(request))
                .map(taskMapper::toResponse)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/addObserver")
    public Mono<ResponseEntity<TaskResponse>> addObserver(@PathVariable("id") String taskId, @RequestParam String userId) {
        return taskService.addObserver(taskId, userId)
                .map(taskMapper::toResponse)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> delete(@PathVariable String id) {
        return taskService.delete(id)
                .then(Mono.just(ResponseEntity.noContent().build()));
    }

}
