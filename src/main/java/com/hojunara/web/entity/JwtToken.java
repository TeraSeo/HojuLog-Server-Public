package com.hojunara.web.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class JwtToken {

    String grantType;
    String accessToken;
    String refreshToken;

}