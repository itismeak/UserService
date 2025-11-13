package com.microservice.user_service.modules.address.entity;

import com.microservice.user_service.common.entity.BaseEntity;
import com.microservice.user_service.modules.user.entity.User;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Address extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String address1;
    private String address2;
    private String address3;
    private String country;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}