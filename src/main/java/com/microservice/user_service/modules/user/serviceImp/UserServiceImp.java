package com.microservice.user_service.modules.user.serviceImp;

import com.microservice.user_service.common.dto.UserDto;
import com.microservice.user_service.common.dto.UserRequestDto;
import com.microservice.user_service.common.enums.Role;
import com.microservice.user_service.common.enums.UserStatus;
import com.microservice.user_service.common.mapper.UserMapper;
import com.microservice.user_service.modules.address.entity.Address;
import com.microservice.user_service.modules.user.entity.User;
import com.microservice.user_service.modules.user.repository.UserRepository;
import com.microservice.user_service.modules.user.service.UserService;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImp implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    public void init() {
        String defaultUser="admin@gmail.com";
        if(!userRepository.existsByEmail(defaultUser)){
            User user=new User();
            user.setName("Admin");
            user.setEmail(defaultUser);
            user.setPassword(passwordEncoder.encode("123qwe"));
            user.setRole(Role.User);
            user.setStatus(UserStatus.ACTIVE);
            userRepository.save(user);
            System.out.println("Default admin user inserted");
        }
    }

    @Override
    @Transactional
    public UserDto saveUser(UserRequestDto requestDto) {
        log.info("Saving new user: {}", requestDto.getEmail());
        User user = userMapper.toUser(requestDto);
        User savedUser = userRepository.save(user);
        log.info("User {} save successfully: ", requestDto.getEmail());
        return userMapper.toUserDto(savedUser);
    }

    @Override
    public UserDto getUser(Long id) {
        User existUser = userRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("User not found given id: {}", id);
                    return new RuntimeException("User not found: " + id);
                });
        return userMapper.toUserDto(existUser);
    }

    @Override
    @Transactional
    public UserDto updateUser(Long id, UserRequestDto requestDto) {
        User existUser = userRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("User not found given id: {}", id);
                    return new RuntimeException("User not found: " + id);
                });
        String psw=passwordEncoder.encode(requestDto.getPassword());
        existUser.setName(requestDto.getName());
        existUser.setEmail(requestDto.getEmail());
        existUser.setPassword(psw);
        existUser.setRole(requestDto.getRole());
        existUser.setStatus(requestDto.getStatus());
        List<Address> addressList=requestDto.getAddress().stream()
                        .map(userMapper::toAddress)
                                .toList();
        existUser.setAddresses(addressList);
        log.info("Updating user ID: {}", id);
        User savedUser=userRepository.save(existUser);
        log.info("User updated successfully: {}", id);
        return userMapper.toUserDto(savedUser);
    }

    @Override
    public Page<UserDto> getAllUsers(int page, int size, String search) {
        Pageable pageable = PageRequest.of(page, size);
        Page<User> users;
        if (search == null || search.isEmpty()) {
            users = userRepository.findAll(pageable);
        } else {
            users = userRepository.findByNameContainingIgnoreCaseOrEmailContainingIgnoreCase(
                    search, search, pageable);
        }
        log.info("Fetched {} users for page {}", users.getNumberOfElements(), page);
        return users.map(userMapper::toUserDto);
    }

    @Override
    @Transactional
    public void softDelete(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found: " + id));
        user.setStatus(UserStatus.DELETED);
        userRepository.save(user);
        log.info("Soft deleted user ID: {}", id);
    }

    @Override
    @Transactional
    public void hardDelete(Long id) {
        if (!userRepository.existsById(id)) {
            log.warn("User not found for hard delete: {}", id);
            return;
        }
        userRepository.deleteById(id);
        log.info("Hard deleted user ID: {}", id);
    }
}