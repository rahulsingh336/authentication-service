package com.luxoft.authentication.service.utils;

import com.luxoft.authentication.service.dto.ExtendedUserDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import io.jsonwebtoken.*;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;


@Component
@Slf4j
public class JwtTokenUtils {

    @Value("${authentication.app.jwtSecret}")
    private String jwtSecret;

    @Value("${authentication.app.jwtExpirationMins}")
    private int jwtExpirationMins;

    public String generateJwtToken(Authentication authentication, List<String> roles) {

        ExtendedUserDetails userPrincipal = (ExtendedUserDetails) authentication.getPrincipal();
        LocalDateTime dateTime = LocalDateTime.now().plus(Duration.of(jwtExpirationMins, ChronoUnit.MINUTES));
        Date jwtExpiration = Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant());
        return Jwts.builder()
                .setSubject((userPrincipal.getUsername()))
                .claim("roles", roles)
                .setIssuedAt(new Date())
                .setExpiration(jwtExpiration)
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public String getUserNameFromJwtToken(String token) {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            log.info("Token is valid");
            return true;
        } catch (SignatureException e) {
            log.error("Invalid JWT signature: {}", e.getMessage());
            throw e;
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
            throw e;
        } catch (ExpiredJwtException e) {
            log.error("JWT token is expired: {}", e.getMessage());
            throw e;
        } catch (UnsupportedJwtException e) {
            log.error("JWT token is unsupported: {}", e.getMessage());
            throw e;
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty: {}", e.getMessage());
            throw e;
        }
    }

    public String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");

        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }

        return null;
    }

}
