package com.microservice.user_service.modules.user.repository;

import com.microservice.user_service.modules.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    Page<User> findByNameContainingIgnoreCaseOrEmailContainingIgnoreCase(String search, String search1, Pageable pageable);
}