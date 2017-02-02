package com.obs.security.serivce.impl;

import com.obs.repo.UserRepo;
import com.obs.security.User;
import com.obs.security.serivce.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class UserDetailsServiceImpl implements CustomUserDetailsService {
    @Autowired
    private UserRepo userRepo;

    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepo.readByUsername(username)
                .map(user -> new User(user.getId(), username, user.getPassword(), user.getEmail(),
                        user.getRoles().stream().map(role -> new SimpleGrantedAuthority(role.getName()))
                                .collect(Collectors.toSet()))
                ).orElseThrow(() -> new UsernameNotFoundException(username + " cannot be found"));
    }
}
