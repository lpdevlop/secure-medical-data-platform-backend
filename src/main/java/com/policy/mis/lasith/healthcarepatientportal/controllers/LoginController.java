package com.policy.mis.lasith.healthcarepatientportal.controllers;

import com.policy.mis.lasith.healthcarepatientportal.database.dtos.LoginRequest;
import com.policy.mis.lasith.healthcarepatientportal.database.dtos.LoginResponse;
import com.policy.mis.lasith.healthcarepatientportal.services.LoginService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class LoginController {

    private final LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {

        LoginResponse response = loginService.login(request);

        return ResponseEntity.ok(response);
    }
}