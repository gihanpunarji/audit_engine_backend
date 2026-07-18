package com.gihan.AIAuditEngine.service;

import com.gihan.AIAuditEngine.entity.User;
import com.gihan.AIAuditEngine.entity.UserPrinciple;
import com.gihan.AIAuditEngine.repository.UserRepo;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailService implements UserDetailsService {

    private UserRepo userRepo;

    @Autowired
    public MyUserDetailService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public @NonNull UserDetails loadUserByUsername(@NonNull String username) throws UsernameNotFoundException {
        User user = userRepo.findByEmail(username);

        if(user != null) {
            return new UserPrinciple(user);
        } else {
            throw new UsernameNotFoundException("User Not Found");
        }
    }
}
