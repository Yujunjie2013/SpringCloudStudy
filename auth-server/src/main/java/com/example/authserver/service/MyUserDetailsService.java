package com.example.authserver.service;

import com.junjie.common.feign.UserService;
import com.junjie.common.model.LoginAppUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.social.security.SocialUser;
import org.springframework.social.security.SocialUserDetails;
import org.springframework.social.security.SocialUserDetailsService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class MyUserDetailsService implements MobileUserDetailsService, SocialUserDetailsService {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Resource
    private UserService userService;

    /**
     * 表单登录用的
     *
     * @param userName
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        String encode = passwordEncoder.encode("123456");
        logger.info("表单登录用户名:" + userName + "---数据库密码是:" + encode);
        return new User(userName, encode, true, true,
                true, true,
                AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_USER"));
    }

    /**
     * 社交登录用的
     *
     * @param userId
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public SocialUserDetails loadUserByUserId(String userId) throws UsernameNotFoundException {
        String encode = passwordEncoder.encode("123456");
        logger.info("社交登录Id:" + userId + "---数据库密码是:" + encode);
        return new SocialUser(userId, encode, true, true,
                true, true,
                AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_USER"));
    }

    @Override
    public UserDetails loadUserByMobile(String mobile) {
        LoginAppUser loginAppUser = userService.findByMobile(mobile);
        return checkUser(loginAppUser);
    }

    private LoginAppUser checkUser(LoginAppUser loginAppUser) {
        if (loginAppUser != null && !loginAppUser.isEnabled()) {
            throw new DisabledException("用户已作废");
        }
        return loginAppUser;
    }
}
