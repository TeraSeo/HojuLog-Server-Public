package com.hojunara.web.service;

import com.hojunara.web.entity.Otp;

public interface OtpService {
    Otp getOtpByEmail(String email);

    void sendOtp(String email);

    String createCode();

    void saveOtp(String email, String code);

    Boolean checkOtp(String email, String code);
}
