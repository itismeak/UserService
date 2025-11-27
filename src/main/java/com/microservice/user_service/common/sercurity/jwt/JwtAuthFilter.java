package com.microservice.user_service.common.sercurity.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservice.user_service.common.constants.AppConstant;
import com.microservice.user_service.common.dto.ApiResponse;
import com.microservice.user_service.common.sercurity.service.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Instant;
import java.util.Arrays;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService userDetailsService;
    private static final AntPathMatcher pathMatcher = new AntPathMatcher();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String path = request.getRequestURI();

        //Allow public endpoints (VERY IMPORTANT)
        if (isPublicEndpoint(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        //Validate Authorization header
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            handleException(response,
                    "You need to log in to access this page.",
                    HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        String token = authHeader.substring(7);

        // Validate token
        if (!jwtUtil.validateToken(token)) {
            handleException(response,
                    "Invalid or expired token.",
                    HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        // Extract user and set security context
        String email = jwtUtil.extractEmail(token);

        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            UserDetails userDetails = userDetailsService.loadUserByUsername(email);

            var authToken = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities()
            );
            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContextHolder.getContext().setAuthentication(authToken);
        }

        filterChain.doFilter(request, response);
    }

    private boolean isPublicEndpoint(HttpServletRequest request) {
        String path = request.getRequestURI();
        return Arrays.stream(AppConstant.PUBLIC_ENDPOINTS)
                .anyMatch(pattern -> pathMatcher.match(pattern, path));
    }

    private void handleException(HttpServletResponse response, String userFriendlyMessage, int status)
            throws IOException {

        response.setStatus(status);
        response.setContentType("application/json");

        ApiResponse<Map<String, Object>> apiResponse = new ApiResponse<>(
                userFriendlyMessage,
                Map.of(
                        "timestamp", Instant.now().toString(),
                        "status", status,
                        "error", HttpStatus.valueOf(status).getReasonPhrase()
                ),
                false
        );

        response.getWriter().write(new ObjectMapper().writeValueAsString(apiResponse));
    }
}