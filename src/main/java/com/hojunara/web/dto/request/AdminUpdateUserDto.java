package com.hojunara.web.dto.request;

import com.hojunara.web.entity.Role;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AdminUpdateUserDto {
    private Long userId;

    private Long log;

    private Long likeCountThisWeek;

    private Role role;

    private Boolean isLocked;
}
