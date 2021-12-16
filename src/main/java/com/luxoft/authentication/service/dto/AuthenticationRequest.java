package com.luxoft.authentication.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthenticationRequest implements Serializable {

    private static final long serialVersionUID = -2816303391678229842L;

    @NotBlank
    private String username;

    @NotBlank
    private String password;

}
