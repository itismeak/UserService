package com.microservice.user_service.common.dto;

import com.microservice.user_service.common.enums.Role;
import com.microservice.user_service.common.enums.UserStatus;
import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
public class UserDto {
    private Long id;
    private String name;
    private String email;
    private Role role;
    private UserStatus status;
    private List<AddressDto> address;

    // Audit fields
    private Instant createdAt;
    private Instant updatedAt;
    private String createdBy;
    private String updatedBy;
}