package com.java.assessment.reviewbox.global.security.holder;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationHolder {
    public long getUserId() {
        return (long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
