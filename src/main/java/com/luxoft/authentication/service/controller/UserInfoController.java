package com.luxoft.authentication.service.controller;

import com.luxoft.authentication.service.dto.UserDetailDTO;
import com.luxoft.authentication.service.service.UserInfoService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@Slf4j
@AllArgsConstructor
public class UserInfoController {

    private UserInfoService userInfoService;

    @GetMapping("getUserInfo")
    @PreAuthorize("hasAnyAuthority('agent', 'admin')")
    public UserDetailDTO getUserInfo() {
     return userInfoService.getUserInfo();
    }

}
