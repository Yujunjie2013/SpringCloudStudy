package com.junjie.common.feign.fallback;

import com.junjie.common.feign.UserService;
import com.junjie.common.model.LoginAppUser;
import com.junjie.common.model.SysUser;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;

/**
 * userService降级工场
 */
@Slf4j
//public class UserServiceFallbackFactory implements FallbackFactory<UserService> {
public class UserServiceFallbackFactory implements UserService {
//    @Override
//    public UserService create(Throwable throwable) {
//        return new UserService() {
            @Override
            public SysUser selectByUsername(String username) {
//                log.error("通过用户名查询用户异常:{}", username, throwable);
                log.error("通过用户名查询用户异常:{}", username);
                return new SysUser();
            }

            @Override
            public LoginAppUser findByUsername(String username) {
//                log.error("通过用户名查询用户异常:{}", username, throwable);
                log.error("通过用户名查询用户异常:{}", username);
                return new LoginAppUser();
            }

            @Override
            public LoginAppUser findByMobile(String mobile) {
                log.error("通过手机号查询用户异常:{}", mobile);
//                log.error("通过手机号查询用户异常:{}", mobile, throwable);
                return new LoginAppUser();
            }

            @Override
            public LoginAppUser findByOpenId(String openId) {
//                log.error("通过openId查询用户异常:{}", openId, throwable);
                log.error("通过openId查询用户异常:{}", openId);
                return new LoginAppUser();
            }
//        };
//    }
}
