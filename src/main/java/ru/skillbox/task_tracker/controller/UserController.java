package ru.skillbox.task_tracker.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.skillbox.task_tracker.dto.UserRequest;
import ru.skillbox.task_tracker.dto.UserResponse;
import ru.skillbox.task_tracker.mapper.UserMapper;
import ru.skillbox.task_tracker.service.UserService;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    private final UserMapper userMapper;

    @GetMapping
    public Flux<UserResponse> findAll() {
        return userService.findAll().map(userMapper::toResponse);
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<UserResponse>> findById(@PathVariable String id) {
        return userService.findById(id)
                .map(userMapper::toResponse)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Mono<ResponseEntity<UserResponse>> create(@RequestBody UserRequest request) {
        return userService.create(userMapper.toUser(request))
                .map(userMapper::toResponse)
                .map(ResponseEntity::ok);
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<UserResponse>> update(@PathVariable String id, @RequestBody UserRequest request) {
        return userService.update(id, userMapper.toUser(request)).map(userMapper::toResponse).map(ResponseEntity::ok);
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> delete(@PathVariable String id) {
        return userService.deleteById(id).then(Mono.just(ResponseEntity.noContent().build()));
    }

}
