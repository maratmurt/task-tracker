package ru.skillbox.task_tracker.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.skillbox.task_tracker.dto.task.TaskRequest;
import ru.skillbox.task_tracker.dto.task.TaskResponse;
import ru.skillbox.task_tracker.mapper.TaskMapper;
import ru.skillbox.task_tracker.model.Task;
import ru.skillbox.task_tracker.service.TaskService;
import ru.skillbox.task_tracker.service.UserService;

@RestController
@RequestMapping("/api/task")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    private final TaskMapper taskMapper;

    private final UserService userService;

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
    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    public Mono<ResponseEntity<TaskResponse>> create(@RequestBody TaskRequest request,
                                                     @AuthenticationPrincipal UserDetails userDetails) {
        return Mono.just(taskMapper.toTask(request))
                .zipWhen(task -> userService.findByName(userDetails.getUsername()))
                .map(t -> {
                    Task task = t.getT1();
                    task.setAuthorId(t.getT2().getId());
                    return task;
                })
                .flatMap(taskService::create)
                .map(taskMapper::toResponse)
                .map(ResponseEntity::ok);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
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
    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    public Mono<ResponseEntity<Void>> delete(@PathVariable String id) {
        return taskService.delete(id)
                .then(Mono.just(ResponseEntity.noContent().build()));
    }

}
