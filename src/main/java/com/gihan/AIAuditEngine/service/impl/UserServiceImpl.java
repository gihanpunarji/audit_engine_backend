package com.gihan.AIAuditEngine.service.impl;

import com.gihan.AIAuditEngine.dto.UserRequestDTO;
import com.gihan.AIAuditEngine.entity.User;
import com.gihan.AIAuditEngine.mapper.UserMapper;
import com.gihan.AIAuditEngine.repository.UserRepo;
import com.gihan.AIAuditEngine.service.JWTService;
import com.gihan.AIAuditEngine.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;
    private final UserMapper userMapper;
    private final AuthenticationManager authManager;
    private final JWTService jwtService;
    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    @Autowired
    public UserServiceImpl(UserRepo userRepo, UserMapper userMapper, AuthenticationManager authManager, JWTService jwtService) {
        this.userRepo = userRepo;
        this.userMapper = userMapper;
        this.authManager = authManager;
        this.jwtService = jwtService;
    }


    @Override
    public String createUser(UserRequestDTO userRequestDTO) {
        if (userRepo.existsByEmail(userRequestDTO.getEmail())){
            return "User Already Registered";
        } else {
            User user = userMapper.toEntity(userRequestDTO);
            user.setPassword(encoder.encode(user.getPassword()));
            userRepo.save(user);
            return "Saved";
        }
    }

    @Override
    public String verifyUser(UserRequestDTO userRequestDTO) {
        Authentication authentication = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(userRequestDTO.getEmail(), userRequestDTO.getPassword()));
        if (authentication.isAuthenticated()) {
            return jwtService.generateToken(userRequestDTO.getEmail());
        } else {
            return "Fail";
        }
    }

    @Override
    public User getUsers(String email) {
        return userRepo.findByEmail(email);
    }
}
