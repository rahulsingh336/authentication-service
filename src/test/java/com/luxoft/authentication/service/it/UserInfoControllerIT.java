package com.luxoft.authentication.service.it;

import org.junit.Test;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class UserInfoControllerIT extends BaseIT {

    @Test
    public void givenCorrectToken_thenReturnUserInfo() throws Exception {

        final String token = getTokenForLogin("admin@mock.com", "pass", getMockMvc());

        getMockMvc().perform(get("/getUserInfo")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    @Test
    public void givenNoToken_thenReturnError() throws Exception {

        getMockMvc().perform(get("/getUserInfo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + null))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void givenTokenWithVendorRole_thenReturnError() throws Exception {

        final String token = getTokenForLogin("vendor@mock.com", "pass", getMockMvc());

        getMockMvc().perform(get("/getUserInfo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + null))
                .andExpect(status().is4xxClientError());
    }

}
