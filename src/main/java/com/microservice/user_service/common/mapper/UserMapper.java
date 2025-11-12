package com.microservice.user_service.common.mapper;

import com.microservice.user_service.common.dto.UserDto;
import com.microservice.user_service.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    private final ModelMapper modelMapper;

    public UserMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public UserDto toUserDto(User user) {
        if (user == null) return null;
        return modelMapper.map(user, UserDto.class);
    }
}