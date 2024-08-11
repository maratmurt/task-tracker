package ru.skillbox.task_tracker.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import ru.skillbox.task_tracker.dto.UserRequest;
import ru.skillbox.task_tracker.dto.UserResponse;
import ru.skillbox.task_tracker.model.User;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    User toUser(UserRequest request);

    UserResponse toResponse(User user);

}
