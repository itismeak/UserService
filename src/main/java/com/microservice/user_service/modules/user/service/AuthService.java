package com.microservice.user_service.modules.user.service;

import com.microservice.user_service.common.dto.JwtResponse;
import com.microservice.user_service.common.dto.LoginRequestDto;

public interface AuthService {
    JwtResponse loginAuthenticate(LoginRequestDto request);
}
