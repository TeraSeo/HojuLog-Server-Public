package com.hojunara.web.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResponseCommentRequestDto {
    private String content;
    private Long parentCommentId;
    private Long userId;
}