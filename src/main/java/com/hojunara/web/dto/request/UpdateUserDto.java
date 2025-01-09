package com.hojunara.web.dto.request;

import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Builder
public class UpdateUserDto {

    private Long userId;

    private String username;

    private String description;

    MultipartFile profileImg;
}
