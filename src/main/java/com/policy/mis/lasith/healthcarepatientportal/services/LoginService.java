package com.policy.mis.lasith.healthcarepatientportal.services;

import com.policy.mis.lasith.healthcarepatientportal.database.dtos.LoginRequest;
import com.policy.mis.lasith.healthcarepatientportal.database.dtos.LoginResponse;
import com.policy.mis.lasith.healthcarepatientportal.database.entity.User;
import com.policy.mis.lasith.healthcarepatientportal.database.repository.UserRepository;
import com.policy.mis.lasith.healthcarepatientportal.exceptions.CustomException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LoginService {

    private final UserRepository userRepository;
    private final JwtService jwtService;

    public LoginService(UserRepository userRepository, JwtService jwtService) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }


    public LoginResponse login(LoginRequest request) {

        Optional<User> user =userRepository.findUserByNic(request.getNic());

        if(user.isPresent()) {
            String access = jwtService.generateToken(user.get());
            String refresh = jwtService.generateRefreshToken(user.get());

            return LoginResponse.builder()
                    .accessToken(access)
                    .refreshToken(refresh)
                    .fullName(user.get().getUsername())
                    .role(user.get().getRole())
                    .build();
        }else {
            throw new CustomException("User Not Found", HttpStatus.NOT_FOUND);
        }
    }
}