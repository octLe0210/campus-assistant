package com.campus.controller;

import com.campus.common.Result;
import com.campus.dto.LoginDTO;
import com.campus.entity.User;
import com.campus.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public Result<?> login(@RequestBody LoginDTO loginDTO) {
        try {
            return Result.ok(userService.login(loginDTO.getUsername(), loginDTO.getPassword()));
        } catch (RuntimeException e) {
            return Result.error(400, e.getMessage());
        }
    }

    @PostMapping("/register")
    public Result<?> register(@RequestBody User user) {
        try {
            return Result.ok(userService.register(user));
        } catch (RuntimeException e) {
            return Result.error(400, e.getMessage());
        }
    }

    @GetMapping("/info")
    public Result<?> info(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        return Result.ok(userService.getById(userId));
    }
}
