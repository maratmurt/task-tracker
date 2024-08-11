package ru.skillbox.task_tracker.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.skillbox.task_tracker.mapper.NullAwareMapper;
import ru.skillbox.task_tracker.model.User;
import ru.skillbox.task_tracker.repository.UserRepository;

import java.lang.reflect.InvocationTargetException;
import java.text.MessageFormat;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;

    private final NullAwareMapper nullAwareMapper;

    public Flux<User> findAll() {
        return userRepository.findAll();
    }

    public Mono<User> findById(String id) {
        return userRepository.findById(id);
    }

    public Mono<User> create(User user) {
        return userRepository.save(user);
    }

    public Mono<User> update(String id, User user) {
        return userRepository.findById(id).switchIfEmpty(Mono.error(new Exception(MessageFormat.format("Пользователь с ID {0} не найден", id)))).flatMap(existingUser -> {
            try {
                nullAwareMapper.copyProperties(existingUser, user);
                log.info(existingUser.toString());
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
            return userRepository.save(existingUser);
        });
    }

    public Mono<Void> deleteById(String id) {
        return userRepository.deleteById(id);
    }

}
