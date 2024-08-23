package ru.skillbox.task_tracker.dto.user;

import java.util.List;

public record UserRequest(String username, String email, String password, List<String> roles) {
}
