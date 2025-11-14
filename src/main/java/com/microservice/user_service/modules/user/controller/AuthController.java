package com.microservice.user_service.modules.user.controller;

import com.microservice.user_service.common.constants.AppConstant;
import com.microservice.user_service.common.dto.ApiResponse;
import com.microservice.user_service.common.dto.JwtResponse;
import com.microservice.user_service.common.dto.LoginRequestDto;
import com.microservice.user_service.modules.user.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(AppConstant.apiVersion+"/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<JwtResponse>> login(
            @Valid @RequestBody LoginRequestDto request) {

        log.info("🔐 Login attempt for email={}", request.getEmail());

        JwtResponse response = authService.loginAuthenticate(request);

        log.info("✅ Login success for email={}", request.getEmail());

        return ResponseEntity.ok(new ApiResponse<>( "Login successful", response,true));
    }
}