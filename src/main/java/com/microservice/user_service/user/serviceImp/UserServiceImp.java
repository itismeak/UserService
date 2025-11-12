package com.microservice.user_service.user.serviceImp;

import com.microservice.user_service.common.dto.UserDto;
import com.microservice.user_service.common.dto.UserRequestDto;
import com.microservice.user_service.common.mapper.UserMapper;
import com.microservice.user_service.user.controller.UserController;
import com.microservice.user_service.user.entity.User;
import com.microservice.user_service.user.repository.UserRepository;
import com.microservice.user_service.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImp implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @Override
    public UserDto createUser(UserRequestDto request) {
        log.info("Creating user with email: {}", request.getEmail());

        userRepository.findByEmail(request.getEmail()).ifPresent(u -> {
            throw new RuntimeException("Email already exists!");
        });

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword()); // Ideally hash the password

        User savedUser = userRepository.save(user);
        log.info("User created with id: {}", savedUser.getId());

        return userMapper.toUserDto(savedUser);
    }

    @Override
    public UserDto getUserById(Long id) {
        log.info("Fetching user with id: {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return userMapper.toUserDto(user);
    }

    @Override
    public UserDto updateUser(Long id, UserRequestDto request) {
        log.info("Updating user with id: {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());

        User updatedUser = userRepository.save(user);
        log.info("User updated with id: {}", updatedUser.getId());
        return userMapper.toUserDto(updatedUser);
    }

    @Override
    public void deleteUser(Long id) {
        log.info("Deleting user with id: {}", id);
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found");
        }
        userRepository.deleteById(id);
        log.info("User deleted with id: {}", id);
    }

    @Override
    public List<UserDto> getAllUsers(int page, int size) {
        log.info("Fetching all users with pagination: page={}, size={}", page, size);
        PageRequest pageable = PageRequest.of(page, size);
        List<User> users = userRepository.findAll(pageable).getContent();
        log.info("Fetched {} users from database", users.size());
        return users.stream().map(userMapper::toUserDto).toList();
    }
}
