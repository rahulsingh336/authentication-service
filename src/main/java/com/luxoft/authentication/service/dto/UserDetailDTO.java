package com.luxoft.authentication.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDetailDTO implements Serializable {

    private static final long serialVersionUID = 6706770409651494898L;

    private Long id;
    private String firstName;
    private String lastName;
    private Integer age;

}
