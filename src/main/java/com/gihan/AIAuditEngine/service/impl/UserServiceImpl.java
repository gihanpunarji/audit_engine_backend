package com.gihan.AIAuditEngine.service.impl;

import com.gihan.AIAuditEngine.dto.UserRequestDTO;
import com.gihan.AIAuditEngine.entity.User;
import com.gihan.AIAuditEngine.mapper.UserMapper;
import com.gihan.AIAuditEngine.repository.UserRepo;
import com.gihan.AIAuditEngine.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;
    private final UserMapper userMapper;

    @Autowired
    public UserServiceImpl(UserRepo userRepo, UserMapper userMapper) {
        this.userRepo = userRepo;
        this.userMapper = userMapper;
    }

    @Override
    public String createUser(UserRequestDTO userRequestDTO) {
        if (userRepo.existsByEmail(userRequestDTO.getEmail())){
            return "User Already Registered";
        } else {
            User user = userMapper.toEntity(userRequestDTO);
            userRepo.save(user);
            return "Saved";
        }

    }
}
