package com.junjie.product.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.oauth2.OAuth2ClientProperties;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordAccessTokenProvider;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordResourceDetails;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class ExampleController {

    @Autowired
    private OAuth2ClientProperties oauth2ClientProperties;

    @Value("${security.oauth2.client.access-token-uri}")
    private String accessTokenUri;


    @RequestMapping("/info")
    public Authentication info(Authentication authentication) {
        return authentication;
    }

    @GetMapping("/hello")
    public String hello() {
        return "world";
    }

    @GetMapping("/admin-list")
    @PreAuthorize("hasRole('ADMIN')") // 要求管理员 ROLE_ADMIN 角色
    public String adminList() {
        return "管理员列表";
    }

    @GetMapping("/user-list")
    @PreAuthorize("hasRole('USER')") // 要求普通用户 ROLE_USER 角色
    public String userList() {
        return "用户列表";
    }

//    @PostMapping("/login")
//    public OAuth2AccessToken login(@RequestParam("username") String username,
//                                   @RequestParam("password") String password) {
//        // <1> 创建 ResourceOwnerPasswordResourceDetails 对象
//        ResourceOwnerPasswordResourceDetails resourceDetails = new ResourceOwnerPasswordResourceDetails();
//        resourceDetails.setAccessTokenUri(accessTokenUri);
//        resourceDetails.setClientId(oauth2ClientProperties.getClientId());
//        resourceDetails.setClientSecret(oauth2ClientProperties.getClientSecret());
//        resourceDetails.setUsername(username);
//        resourceDetails.setPassword(password);
//        // <2> 创建 OAuth2RestTemplate 对象
//        OAuth2RestTemplate restTemplate = new OAuth2RestTemplate(resourceDetails);
//        restTemplate.setAccessTokenProvider(new ResourceOwnerPasswordAccessTokenProvider());
//        // <3> 获取访问令牌
//        return restTemplate.getAccessToken();
//    }
}