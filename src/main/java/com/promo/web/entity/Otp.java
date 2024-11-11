package com.promo.web.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "otp")
@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Otp {

    @Id
    @Column(name = "email")
    private String email;

    @Column(nullable = false)
    private String otpCode;
}
