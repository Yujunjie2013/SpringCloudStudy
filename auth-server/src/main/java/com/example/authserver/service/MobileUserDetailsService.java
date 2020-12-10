package com.example.authserver.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface MobileUserDetailsService extends UserDetailsService {
    /**
     * 根据电话号码查询用户
     */
    UserDetails loadUserByMobile(String mobile);
}
