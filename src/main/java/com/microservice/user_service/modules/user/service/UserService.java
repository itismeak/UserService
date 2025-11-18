package com.microservice.user_service.modules.user.service;

import com.microservice.user_service.common.dto.UserRequestDto;
import com.microservice.user_service.common.dto.UserViewDto;
import org.springframework.data.domain.Page;

public interface UserService {
    UserViewDto saveUser(UserRequestDto requestDto);
    UserViewDto getUser(Long id);
    UserViewDto updateUser(Long id,UserRequestDto requestDto);
    Page<UserViewDto> getAllUsers( int page, int size,
                               String search);
    void softDelete(Long id);
    void hardDelete(Long id);
}
