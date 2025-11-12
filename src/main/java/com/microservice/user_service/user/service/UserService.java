package com.microservice.user_service.user.service;

import com.microservice.user_service.common.dto.UserDto;
import com.microservice.user_service.common.dto.UserRequestDto;

import java.util.List;

public interface UserService {
    UserDto createUser(UserRequestDto request);
    UserDto getUserById(Long id);
    UserDto updateUser(Long id, UserRequestDto request);
    void deleteUser(Long id);
    List<UserDto> getAllUsers(int page, int size);;
}
