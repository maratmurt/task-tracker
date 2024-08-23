package ru.skillbox.task_tracker.dto.user;

import java.util.List;

public record UserResponse(String id, String username, String email, List<String> roles) {
}
