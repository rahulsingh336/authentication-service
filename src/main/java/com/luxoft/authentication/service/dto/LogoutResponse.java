package com.luxoft.authentication.service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.io.Serializable;

@Getter
@AllArgsConstructor
public class LogoutResponse implements Serializable {

    private static final long serialVersionUID = 7185385342104391852L;

    private Boolean status;
}
