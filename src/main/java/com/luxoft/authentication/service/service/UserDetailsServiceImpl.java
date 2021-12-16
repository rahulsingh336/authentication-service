package com.luxoft.authentication.service.service;

import com.luxoft.authentication.service.dto.ExtendedUserDetails;
import com.luxoft.authentication.service.model.User;
import com.luxoft.authentication.service.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByusername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));

        return ExtendedUserDetails.build(user);
    }
}

