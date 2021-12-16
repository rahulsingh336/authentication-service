package com.luxoft.authentication.service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.luxoft.authentication.service.dto.AuthenticationRequest;
import com.luxoft.authentication.service.service.AuthenticationService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import java.nio.charset.StandardCharsets;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(excludeAutoConfiguration = SecurityAutoConfiguration.class)
@Import(AuthenticationController.class)
public class AuthenticationControllerTest {

    private static final String MOCK_TOKEN = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbkBtb2NrLmNvbSIsInJvbGVzIjpbImFkbWluIl0sImlhdCI6MTYzOTU3MjM5MSwiZXhwIjoxNjM5NTcyNDUxfQ.hvtHNVVRYlXguIV3qPLH410W06AHH2r4sA-Aa73FPbnlmHCc0RH0KxvPnlvnXsLaM6UUNs1hY0gZP1Z6gF3LuA";

    @Autowired
    private MockMvc mvc;

    @MockBean
    private AuthenticationService authenticationService;

    @Autowired
    private ObjectMapper mapper;

    @Test
    public void givenCorrectCredentials_thenReturnToken()
            throws Exception {
        AuthenticationRequest authenticationRequest = AuthenticationRequest.builder().username("MOCK").password("MOCK_PWD").build();
        given(authenticationService.generateTokenAndReturnToken(any())).willReturn(MOCK_TOKEN);

        mvc.perform(post("/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(authenticationRequest).getBytes(StandardCharsets.UTF_8)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.jwtToken", is(MOCK_TOKEN)));
    }

    @Test
    public void givenInCorrectCredentials_thenReturnError()
            throws Exception {
        AuthenticationRequest authenticationRequest = AuthenticationRequest.builder().username("MOCK").password("INVALID_PWD").build();
        given(authenticationService.generateTokenAndReturnToken(any())).willThrow(new BadCredentialsException("Incorrect cred."));

        mvc.perform(post("/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(authenticationRequest).getBytes(StandardCharsets.UTF_8)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockUser(username = "user", password = "pwd", roles = "USER")
    public void givenToken_thenReturnLogoutSuccess()
            throws Exception {
        AuthenticationRequest authenticationRequest = AuthenticationRequest.builder().username("MOCK").password("MOCK_PWD").build();
        given(authenticationService.removeToken(any())).willReturn(Boolean.TRUE);

        mvc.perform(delete("/signout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + "MOCK_TOKEN"))
                .andExpect(status().is2xxSuccessful());
    }

    @EnableWebSecurity
    @EnableWebMvc
    static class Config extends WebSecurityConfigurerAdapter {

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.cors().and().csrf().disable()
                    .authorizeRequests().antMatchers("/authenticate").permitAll()
                    .anyRequest().authenticated()
                    .and().sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        }

        @Autowired
        void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
            auth
                    .inMemoryAuthentication()
                    .withUser("user").password("pwd").roles("USER");
        }

        @Override
        @Bean
        public UserDetailsService userDetailsServiceBean() throws Exception {
            return super.userDetailsServiceBean();
        }
    }

}
