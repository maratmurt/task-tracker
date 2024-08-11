package ru.skillbox.task_tracker.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.ReadOnlyProperty;

import java.time.Instant;
import java.util.Set;

@Data
public class Task {

    @Id
    private String id;

    private String name;

    private String description;

    private Instant createdAt;

    private Instant updatedAt;

    private TaskStatus status;

    private String authorId;

    private String assigneeId;

    private Set<String> observerIds;

    @ReadOnlyProperty
    private User author;

    @ReadOnlyProperty
    private User assignee;

    @ReadOnlyProperty
    private Set<User> observers;

}
