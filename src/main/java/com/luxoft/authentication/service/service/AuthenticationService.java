package com.luxoft.authentication.service.service;

import com.luxoft.authentication.service.dto.AuthenticationRequest;
import com.luxoft.authentication.service.dto.ExtendedUserDetails;
import com.luxoft.authentication.service.repository.UserRepository;
import com.luxoft.authentication.service.utils.JwtTokenUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;
import static com.luxoft.authentication.service.utils.Constants.USER_PREFIX;

@Service
@Slf4j
@AllArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;

    private final AuthenticationManager authenticationManager;

    private final JwtTokenUtils jwtTokenUtil;

    private final RedisTemplate<String, String> redisTemplate;


    public String generateTokenAndReturnToken(AuthenticationRequest authenticationRequest) {
        log.info("Generating token for {}", authenticationRequest.getUsername());
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        ExtendedUserDetails userDetails = (ExtendedUserDetails) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        String generateJwtToken = jwtTokenUtil.generateJwtToken(authentication, roles);
        log.info("Storing token in redis cache for {}", authenticationRequest.getUsername());
        //add to redis cache
        this.redisTemplate.opsForZSet().add(USER_PREFIX + userDetails.getUsername(), generateJwtToken, System.currentTimeMillis());
        return generateJwtToken;
    }

    public Boolean removeToken(HttpServletRequest request) {

        String token = jwtTokenUtil.parseJwt(request);
        String username = jwtTokenUtil.getUserNameFromJwtToken(token);
        String key = USER_PREFIX + username;
        log.info("Deleting token for {}", key);
        // remove from cache
        this.redisTemplate.opsForZSet().remove(key, token);
        return Boolean.TRUE;
    }
}
