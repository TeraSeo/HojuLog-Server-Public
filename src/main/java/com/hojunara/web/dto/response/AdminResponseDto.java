package com.hojunara.web.dto.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class AdminResponseDto {
    List<Long> userIds;

    List<Long> inquiryIds;
}
