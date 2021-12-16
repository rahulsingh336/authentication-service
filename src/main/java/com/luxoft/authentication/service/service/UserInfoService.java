package com.luxoft.authentication.service.service;

import com.luxoft.authentication.service.dto.ExtendedUserDetails;
import com.luxoft.authentication.service.dto.UserDetailDTO;
import com.luxoft.authentication.service.exception.UserNotFoundException;
import com.luxoft.authentication.service.model.UserDetails;
import com.luxoft.authentication.service.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class UserInfoService {

    private final UserRepository userRepository;

    public UserDetailDTO getUserInfo() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ExtendedUserDetails userDetails = (ExtendedUserDetails) principal;
        log.info("Getting UserInfo for {}", userDetails.getUsername());
        UserDetails details = userRepository.findByusername(userDetails.getUsername())
                .orElseThrow(() -> new UserNotFoundException("User Details Not Found" + ((UserDetails) principal).getUsername())).getUserDetails();
        log.info("Converting to DTO for username {}", userDetails.getUsername());
        return toDTO(details);
    }

    private UserDetailDTO toDTO(UserDetails details) {
        return new UserDetailDTO(details.getId(), details.getFirstName(), details.getLastName(), details.getAge());
    }
}
