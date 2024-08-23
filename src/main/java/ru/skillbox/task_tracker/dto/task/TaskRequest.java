package ru.skillbox.task_tracker.dto.task;

import java.util.List;

public record TaskRequest(

    String name,

    String description,

    String status,

    String assigneeId,

    List<String> observerIds

){}
