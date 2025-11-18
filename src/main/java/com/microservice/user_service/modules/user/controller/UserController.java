package com.microservice.user_service.modules.user.controller;

import com.microservice.user_service.common.constants.AppConstant;
import com.microservice.user_service.common.dto.UserRequestDto;
import com.microservice.user_service.common.dto.UserViewDto;
import com.microservice.user_service.modules.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(AppConstant.apiVersion+"/user")
public class UserController {
    private final UserService userService;

    // CREATE
    @PostMapping("/save")
    public ResponseEntity<UserViewDto> createUser(@RequestBody UserRequestDto requestDto) {
        log.info("Received request to create user: {}", requestDto.getEmail());
        UserViewDto userDto = userService.saveUser(requestDto);
        log.info("User created successfully: {}", userDto.getId());
        return ResponseEntity.ok(userDto);
    }

    // READ
    @GetMapping("/getUser/{id}")
    public ResponseEntity<UserViewDto> getUser(@PathVariable Long id) {
        log.info("Fetching user with ID: {}", id);
        UserViewDto userDto = userService.getUser(id);
        log.info("User fetched successfully: {}", id);
        return ResponseEntity.ok(userDto);
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<UserViewDto> updateUser(@PathVariable Long id,
                                              @RequestBody UserRequestDto requestDto) {
        log.info("Updating user with ID: {}", id);
        UserViewDto userDto = userService.updateUser(id, requestDto);
        log.info("User updated successfully: {}", id);
        return ResponseEntity.ok(userDto);
    }

    // LIST WITH PAGINATION & SEARCH
    @GetMapping
    public ResponseEntity<Page<UserViewDto>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "") String search) {

        log.info("Fetching users page={}, size={}, search={}", page, size, search);
        Page<UserViewDto> users = userService.getAllUsers(page, size, search);
        log.info("Fetched {} users", users.getNumberOfElements());
        return ResponseEntity.ok(users);
    }

    // SOFT DELETE
    @DeleteMapping("/soft/{id}")
    public ResponseEntity<Void> softDelete(@PathVariable Long id) {
        log.info("Soft deleting user with ID: {}", id);
        userService.softDelete(id);
        log.info("User soft deleted: {}", id);
        return ResponseEntity.noContent().build();
    }

    // HARD DELETE
    @DeleteMapping("/hard/{id}")
    public ResponseEntity<Void> hardDelete(@PathVariable Long id) {
        log.info("Hard deleting user with ID: {}", id);
        userService.hardDelete(id);
        log.info("User hard deleted: {}", id);
        return ResponseEntity.noContent().build();
    }
}