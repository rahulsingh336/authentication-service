package com.luxoft.authentication.service.controller;

import com.luxoft.authentication.service.dto.AuthenticationRequest;
import com.luxoft.authentication.service.dto.AuthenticationResponse;
import com.luxoft.authentication.service.dto.LogoutResponse;
import com.luxoft.authentication.service.service.AuthenticationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Slf4j
@RestController
@CrossOrigin
@AllArgsConstructor
public class AuthenticationController {

    private AuthenticationService authenticationService;

    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    public ResponseEntity<?> authenticate(@Valid @RequestBody AuthenticationRequest authenticationRequest) {
        log.info("Login request received for {}", authenticationRequest.getUsername());
        String token = authenticationService.generateTokenAndReturnToken(authenticationRequest);
        log.info("Login request competed for {}", authenticationRequest.getUsername());
        return ResponseEntity.ok(new AuthenticationResponse(token));
    }

    @RequestMapping(value = "/signout", method = RequestMethod.DELETE)
    public ResponseEntity<?> logout(HttpServletRequest httpServletRequest) {

        Boolean success = authenticationService.removeToken(httpServletRequest);

        return ResponseEntity.ok(new LogoutResponse(success));
    }

}
