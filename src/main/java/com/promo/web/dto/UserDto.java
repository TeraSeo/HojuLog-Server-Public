package com.promo.web.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserDto {
    private String username;
    private String email;
    private String password;
}
