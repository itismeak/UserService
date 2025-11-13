package com.microservice.user_service.modules.user.service;

import com.microservice.user_service.common.dto.UserDto;
import com.microservice.user_service.common.dto.UserRequestDto;
import org.springframework.data.domain.Page;

public interface UserService {
    UserDto saveUser(UserRequestDto requestDto);
    UserDto getUser(Long id);
    UserDto updateUser(Long id,UserRequestDto requestDto);
    Page<UserDto> getAllUsers( int page, int size,
                               String search);
    void softDelete(Long id);
    void hardDelete(Long id);
}
