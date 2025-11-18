package com.microservice.user_service.modules.user.serviceImp;

import com.microservice.user_service.common.dto.JwtResponse;
import com.microservice.user_service.common.dto.LoginRequestDto;
import com.microservice.user_service.common.enums.UserStatus;
import com.microservice.user_service.common.sercurity.jwt.JwtUtil;
import com.microservice.user_service.modules.user.entity.User;
import com.microservice.user_service.modules.user.repository.UserRepository;
import com.microservice.user_service.modules.user.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImp implements AuthService {
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    @Override
    public JwtResponse loginAuthenticate(LoginRequestDto request) {
        User user=userRepository.findByEmail(request.getEmail())
                .orElseThrow(()->{
                    log.warn("Email {} not register",request.getEmail());
                    return new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                            "Email not registered in the system.");
        });
        if (user.getStatus() != UserStatus.Active) {
            log.warn("🚫 Login blocked. User {} is {}", request.getEmail(), user.getStatus());
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                    "Account is not active. Status: " + user.getStatus());
        }
        try {
            log.info("🔑 Authenticating password for email={}", request.getEmail());

            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );

            log.info("🔑 Authentication success for {}", request.getEmail());

        } catch (Exception ex) {
            log.error("❌ Wrong password for {}", request.getEmail());
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                    "Invalid email or password");
        }

        String token = jwtUtil.generateToken(user);

        log.info("🎟️ JWT generated for {}", request.getEmail());

        return new JwtResponse(
                token,

                "Bearer",
                user.getEmail(),
                user.getRole().name()
        );
    }
}
