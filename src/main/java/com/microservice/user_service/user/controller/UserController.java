package com.microservice.user_service.user.controller;

import com.microservice.user_service.common.constants.AppConstant;
import com.microservice.user_service.common.dto.ApiResponse;
import com.microservice.user_service.common.dto.UserDto;
import com.microservice.user_service.common.dto.UserRequestDto;
import com.microservice.user_service.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(AppConstant.apiVersion+"/user")
public class UserController {
    private final UserService userService;
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    // ✅ CREATE USER
    @PostMapping("/save")
    public ResponseEntity<ApiResponse<UserDto>> createUser(@RequestBody UserRequestDto request) {
        log.info("Received request to create user: {}", request.getEmail());
        UserDto createdUser = userService.createUser(request);
        ApiResponse<UserDto> response = ApiResponse.success("User created successfully", createdUser);
        log.info("Returning response for created user id: {}", createdUser.getId());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // ✅ GET USER BY ID
    @GetMapping("/getById/{id}")
    public ResponseEntity<ApiResponse<UserDto>> getUserById(@PathVariable Long id) {
        log.info("Received request to fetch user with id: {}", id);
        UserDto user = userService.getUserById(id);
        ApiResponse<UserDto> response = ApiResponse.success("User fetched successfully", user);
        return ResponseEntity.ok(response);
    }

    // ✅ UPDATE USER
    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse<UserDto>> updateUser(@PathVariable Long id, @RequestBody UserRequestDto request) {
        log.info("Received request to update user with id: {}", id);
        UserDto updatedUser = userService.updateUser(id, request);
        ApiResponse<UserDto> response = ApiResponse.success("User updated successfully", updatedUser);
        return ResponseEntity.ok(response);
    }

    // ✅ DELETE USER
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable Long id) {
        log.info("Received request to delete user with id: {}", id);
        userService.deleteUser(id);
        ApiResponse<Void> response = ApiResponse.success("User deleted successfully", null);
        return ResponseEntity.ok(response);
    }

    // ✅ GET ALL USERS (WITH PAGINATION)
    @GetMapping("/getAll")
    public ResponseEntity<ApiResponse<Page<UserDto>>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        log.info("Received request to fetch users - page: {}, size: {}", page, size);

        // Using pagination manually from service (if not implemented, we can add it next)
        List<UserDto> users = userService.getAllUsers(page, size);
        Page<UserDto> userPage = new PageImpl<>(users, PageRequest.of(page, size), users.size());

        ApiResponse<Page<UserDto>> response = ApiResponse.success("Users fetched successfully", userPage);
        log.info("Returning {} users for page {}", users.size(), page);
        return ResponseEntity.ok(response);
    }
}
