package com.promo.web.service;

import com.promo.web.entity.Otp;
import com.promo.web.exception.OtpNotFoundException;
import com.promo.web.repository.OtpRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Random;

@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class OtpServiceImpl implements OtpService {

    private final JavaMailSender javaMailSender;
    private final OtpRepository otpRepository;

    @Autowired
    public OtpServiceImpl(JavaMailSender javaMailSender, OtpRepository otpRepository) {
        this.javaMailSender = javaMailSender;
        this.otpRepository = otpRepository;
    }

    @Override
    public Otp getOtpByEmail(String email) {
        try {
            Optional<Otp> o = otpRepository.findById(email);
            if (o.isPresent()) {
                log.info("Successfully found otp with email: {}", email);
                return o.get();
            }
            throw new OtpNotFoundException("Otp not found with email: " + email);
        } catch (Exception e) {
            log.error("Failed to find otp with email: {}", email, e);
            throw e;
        }
    }

    @Override
    public void sendOtp(String email) {
        String code = createCode();
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("seotj0413@gmail.com");
            message.setTo(email);
            message.setSubject("Otp code");
            message.setText("The otp code is " + code);

            javaMailSender.send(message);
            saveOtp(email, code);
            log.info("Successfully sent otp code mail");
        } catch (Exception e) {
            log.error("Failed to send otp code mail");
            throw e;
        }
    }

    @Override
    public String createCode() {
        Random random = new Random();
        String code = String.valueOf(1000 + random.nextInt(9000));
        log.info("Successfully created otp code");
        return code;
    }

    @Override
    public void saveOtp(String email, String code) {
        try {
            Otp otp = Otp.builder().email(email).otpCode(code).build();
            otpRepository.save(otp);
            log.info("Successfully saved otp data");
        } catch (Exception e) {
            log.error("Failed to save otp data");
            throw e;
        }
    }

    @Override
    public Boolean checkOtp(String email, String code) {
        Otp otp = getOtpByEmail(email);
        try {
            String otpCode = otp.getOtpCode();
            if (otpCode.equals(code)) {
                log.info("Otp code was correct");
                return true;
            }
            log.info("Otp code was wrong");
            return false;
        } catch (Exception e) {
            return false;
        }
    }
}
