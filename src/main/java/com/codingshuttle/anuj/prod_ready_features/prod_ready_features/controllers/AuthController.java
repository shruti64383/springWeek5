package com.codingshuttle.anuj.prod_ready_features.prod_ready_features.controllers;

import com.codingshuttle.anuj.prod_ready_features.prod_ready_features.dto.LoginDto;
import com.codingshuttle.anuj.prod_ready_features.prod_ready_features.dto.SignUpDto;
import com.codingshuttle.anuj.prod_ready_features.prod_ready_features.dto.UserDto;
import com.codingshuttle.anuj.prod_ready_features.prod_ready_features.services.AuthService;
import com.codingshuttle.anuj.prod_ready_features.prod_ready_features.services.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.server.Session;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<UserDto> signUp(@RequestBody SignUpDto signUpDto){
        return userService.signUp(signUpDto);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginDto loginDto, HttpServletRequest request, HttpServletResponse response){
        String token = authService.login(loginDto);
        Cookie cookie = new Cookie("token", token);
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
        return ResponseEntity.ok(token);
    }

}
