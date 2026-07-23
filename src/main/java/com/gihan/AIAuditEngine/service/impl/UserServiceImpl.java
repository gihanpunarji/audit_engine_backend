package com.gihan.AIAuditEngine.service.impl;

import com.gihan.AIAuditEngine.dto.UserRequestDTO;
import com.gihan.AIAuditEngine.entity.Organization;
import com.gihan.AIAuditEngine.entity.User;
import com.gihan.AIAuditEngine.entity.UserRole;
import com.gihan.AIAuditEngine.mapper.UserMapper;
import com.gihan.AIAuditEngine.repository.OrganizationRepo;
import com.gihan.AIAuditEngine.repository.UserRepo;
import com.gihan.AIAuditEngine.service.JWTService;
import com.gihan.AIAuditEngine.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;
    private final OrganizationRepo organizationRepo;
    private final UserMapper userMapper;
    private final AuthenticationManager authManager;
    private final JWTService jwtService;
    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    @Autowired
    public UserServiceImpl(UserRepo userRepo,
                           OrganizationRepo organizationRepo,
                           UserMapper userMapper,
                           @Lazy AuthenticationManager authManager,
                           JWTService jwtService) {
        this.userRepo = userRepo;
        this.organizationRepo = organizationRepo;
        this.userMapper = userMapper;
        this.authManager = authManager;
        this.jwtService = jwtService;
    }

    @Transactional
    @Override
    public String createUser(UserRequestDTO userRequestDTO) {
        if (userRepo.existsByEmail(userRequestDTO.getEmail())){
            return "User Already Registered";
        } else {

            Organization organization = new Organization();
            organization.setName(userRequestDTO.getOrganizationName());
            organization.setSlug(userRequestDTO.getOrganizationSlug().toLowerCase().trim());
            organizationRepo.save(organization);

            User user = userMapper.toEntity(userRequestDTO);
            user.setOrganization(organization);
            user.setRole(UserRole.ADMIN);
            user.setPassword(encoder.encode(userRequestDTO.getPassword()));
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
