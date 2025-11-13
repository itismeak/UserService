package com.microservice.user_service.common.dto;

import com.microservice.user_service.common.enums.Role;
import com.microservice.user_service.common.enums.UserStatus;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestDto {
    private String name;
    private String email;
    private String password;
    private Role role;
    private UserStatus status;
    private List<AddressRequestDto> address;
}