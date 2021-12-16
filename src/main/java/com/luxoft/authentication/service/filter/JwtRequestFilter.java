package com.luxoft.authentication.service.filter;

import com.luxoft.authentication.service.utils.JwtTokenUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.luxoft.authentication.service.utils.Constants.USER_PREFIX;
import static java.util.Objects.isNull;

@Component
@Slf4j
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtTokenUtils jwtUtils;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String jwt = jwtUtils.parseJwt(request);
            log.info("Got token from header");
            if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
                String username = jwtUtils.getUserNameFromJwtToken(jwt);
                log.info("Checking if user {} logged out using token ", username);
                // check if token is present in cache and not expired
                if (isNull(redisTemplate.opsForZSet().score(USER_PREFIX + username, jwt))) {
                    //force Login
                    log.warn("User {} logged out using token, continue for login", username);
                    filterChain.doFilter(request, response);
                    return;
                } else {
                    setSecurityContext(request, username);
                }
            }
        } catch (Exception e) {
            //already logged
            throw e;
        }

        filterChain.doFilter(request, response);
    }

    private void setSecurityContext(HttpServletRequest request, String username) {
        log.info("Loading details for {} ", username);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        log.info("Setting security context for {} ", username);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

}
