package com.microservice.user_service.common.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LoginRequestDto {

    @Email(message = "Email should be valid")  // Validates that the email format is correct
    @NotBlank(message = "Email is required")  // Ensures the email is not null or empty
    private String email;

    @NotBlank(message = "Password is required") // Ensures password is not null or empty
    @Size(min = 6, message = "Password must be at least 6 characters")  // Ensures password is at least 6 characters long
    private String password;
}
