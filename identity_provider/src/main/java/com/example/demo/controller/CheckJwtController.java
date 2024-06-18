package com.example.demo.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.User;

@RestController
@RequestMapping("/api/verify")
public class CheckJwtController {
    @GetMapping("/login")
    public Map<String,Long> isLogin(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user=(User) authentication.getPrincipal();
        Map<String,Long> response=new HashMap<>();
        response.put("id", user.getId());
        return response;
    }

    @GetMapping("/admin")
    public HttpStatus isAdmin(){
        return HttpStatus.OK;
    }
}
