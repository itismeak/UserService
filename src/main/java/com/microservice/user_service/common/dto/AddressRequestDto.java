package com.microservice.user_service.common.dto;

import lombok.Data;

@Data
public class AddressRequestDto {
    private Long id;
    private String address1;
    private String address2;
    private String address3;
    private String country;
}
