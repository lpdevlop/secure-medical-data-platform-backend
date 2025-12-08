package com.policy.mis.lasith.healthcarepatientportal.services;

import com.policy.mis.lasith.healthcarepatientportal.database.dtos.LoginRequest;
import com.policy.mis.lasith.healthcarepatientportal.database.dtos.LoginResponse;
import com.policy.mis.lasith.healthcarepatientportal.database.dtos.SignUpRequest;
import com.policy.mis.lasith.healthcarepatientportal.database.entity.User;
import com.policy.mis.lasith.healthcarepatientportal.database.repository.UserRepository;
import com.policy.mis.lasith.healthcarepatientportal.exceptions.CustomException;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class LoginService {

    private final UserRepository userRepository;
    private final JwtService jwtService;

    private final PasswordEncoder passwordEncoder;

    public LoginService(UserRepository userRepository, JwtService jwtService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }


    public User registerUser(SignUpRequest request) {

        if (userRepository.findUserByNic(request.getNic()).isPresent()) {
            throw new RuntimeException("User with this NIC already exists");
        }

        User newUser = User.builder()
                .fullName(request.getFullName())
                .nic(request.getNic())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .dateOfBirth(request.getDateOfBirth())
                .gender(request.getGender())
                .contactNumber(request.getContactNumber())
                .address(request.getAddress())
                .emergencyContact(request.getEmergencyContact())
                .secureId(UUID.randomUUID().toString())
                .build();

        return userRepository.save(newUser);
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