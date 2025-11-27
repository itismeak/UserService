package com.microservice.user_service.common.component;

import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AuditorAwareImpl implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        // SecurityContextHolder.getContext().getAuthentication().getName().
        return Optional.of("system");
    }
}