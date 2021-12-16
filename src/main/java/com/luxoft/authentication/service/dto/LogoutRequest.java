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
public class LogoutRequest implements Serializable {

    private static final long serialVersionUID = 4522924149107031800L;

    @NotBlank
    private String token;
}
