package com.hojunara.web.dto.response;

import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class NotificationDto {
    private Long id;

    private String title;

    private String message;

    private Boolean isRead;

    private Timestamp createdAt;
}
