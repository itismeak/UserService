package com.microservice.user_service.common.mapper;

import com.microservice.user_service.common.dto.AddressDto;
import com.microservice.user_service.common.dto.AddressRequestDto;
import com.microservice.user_service.common.dto.UserDto;
import com.microservice.user_service.common.dto.UserRequestDto;
import com.microservice.user_service.modules.address.entity.Address;
import com.microservice.user_service.modules.user.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class UserMapper {

    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    public UserMapper(PasswordEncoder passwordEncoder, ModelMapper modelMapper) {
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
    }

    public UserDto toUserDto(User user) {
        if (user == null) {
            log.warn("User is null, cannot map to UserDto");
            return null;
        }
        UserDto dto = new UserDto();
        modelMapper.map(user, dto);

        List<Address> addresses = user.getAddresses();
        if(addresses != null && !addresses.isEmpty()){
            List<AddressDto> addressDtos= addresses.stream()
                    .map(this::toAddressDto)
                    .toList();
            dto.setAddress(addressDtos);
        }else {
            dto.setAddress(Collections.emptyList());
            log.info("No addresses found for User [id={}]", user.getId());
        }
        return dto;
    }

    public AddressDto toAddressDto(Address address){
        if(address == null){
            return null;
        }
        AddressDto dto=new AddressDto();
        modelMapper.map(address,dto);
        return dto;
    }

    public User toUser(UserRequestDto requestDto) {
        User user = new User();

        // Map basic fields (exclude addresses)
        user.setName(requestDto.getName());
        user.setEmail(requestDto.getEmail());
        user.setPassword(passwordEncoder.encode(requestDto.getPassword()));
        user.setRole(requestDto.getRole());
        user.setStatus(requestDto.getStatus());

        // Map addresses safely
        if (requestDto.getAddress() != null && !requestDto.getAddress().isEmpty()) {
            List<Address> addresses = requestDto.getAddress().stream()
                    .map(dto -> {
                        Address address = new Address();
                        address.setAddress1(dto.getAddress1());
                        address.setAddress2(dto.getAddress2());
                        address.setAddress3(dto.getAddress3());
                        address.setCountry(dto.getCountry());
                        address.setId(null);
                        address.setUser(user);
                        return address;
                    })
                    .collect(Collectors.toList());

            user.setAddresses(addresses);
        }

        return user;
    }
    public Address toAddress(AddressRequestDto addressDto){
        Address address=new Address();
        modelMapper.map(addressDto,address);
        return address;
    }
}