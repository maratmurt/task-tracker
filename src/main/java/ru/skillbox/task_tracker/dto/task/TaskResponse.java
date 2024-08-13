package ru.skillbox.task_tracker.dto.task;

import ru.skillbox.task_tracker.dto.user.UserResponse;

import java.util.List;

public record TaskResponse(

     String id,

     String name,

     String description,

     Long createdAt,

     Long updatedAt,

     String status,

     UserResponse author,

     UserResponse assignee,

     List<UserResponse> observers

){}
