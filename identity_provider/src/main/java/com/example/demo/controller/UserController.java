package com.example.demo.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.Authentication;

import com.example.demo.dto.EditUserDto;
import com.example.demo.entity.User;
import com.example.demo.service.UserService;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Lazy
    @Autowired
    private PasswordEncoder encoder;
    @Autowired
    UserService userService;

    @PostMapping("/register")
    public boolean createUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    @PutMapping("/edit")
    public ResponseEntity<?> updateUser(@RequestBody EditUserDto userUpdateDetails) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user=(User) authentication.getPrincipal();
        final User updatedUser = userService.updateUser(user.getId(), userUpdateDetails);
        Map<String, Object> data = new HashMap<>();
        
        String token=userService.generateToken(updatedUser);
        data.put("token", token);
        return ResponseEntity.ok(data);
    }

    @DeleteMapping("/remove")
    public Map<String, Boolean> deleteUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user=(User) authentication.getPrincipal();
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", userService.deleteUser(user.getId()));
        return response;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String ,String> credentials){
        String mail = credentials.get("mail");
        String password = credentials.get("password");
        if (userService.checkUserNameExists(mail)) {
            if (userService.verifyUser(mail, password)) {
                String token = userService.generateToken(mail, password);
                Map<String, Object> data = new HashMap<>();
                data.put("token", token);
                return ResponseEntity.ok(data);
            } else {
                return ResponseEntity.badRequest().body("Bad login");
            }
        } else {
            return ResponseEntity.badRequest().body("Bad login");
        }
    }

    @GetMapping
    public ResponseEntity<User> getUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user=(User) authentication.getPrincipal();
        return ResponseEntity.ok(user);
    }
}
