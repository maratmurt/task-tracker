package ru.skillbox.task_tracker.dto.task;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import ru.skillbox.task_tracker.dto.user.UserResponse;

import java.util.List;

@ToString
@EqualsAndHashCode
public class TaskResponse {

    private String id;

    private String name;

    private String description;

    private Long createdAt;

    private Long updatedAt;

    private String status;

    private UserResponse author;

    private UserResponse assignee;

    private List<UserResponse> observers;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    public Long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Long updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public UserResponse getAuthor() {
        return author;
    }

    public void setAuthor(UserResponse author) {
        this.author = author;
    }

    public UserResponse getAssignee() {
        return assignee;
    }

    public void setAssignee(UserResponse assignee) {
        this.assignee = assignee;
    }

    public List<UserResponse> getObservers() {
        return observers;
    }

    public void setObservers(List<UserResponse> observers) {
        this.observers = observers;
    }
}
