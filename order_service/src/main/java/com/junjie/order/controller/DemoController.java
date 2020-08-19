package com.junjie.order.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class DemoController {

    @GetMapping("/admin-list")
    @PreAuthorize("hasRole('ADMIN')")
    public String admin() {
        return "管理员列表访问成功";
    }

    @GetMapping("/user-list")
    public String geust() {
        return "访客列表访问成功";
    }

}
