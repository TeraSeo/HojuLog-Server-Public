package com.hojunara.web.dto.response;

import com.hojunara.web.entity.Role;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class NormalUserDto {
    private Long id;

    private String username;

    private Long log;

    private Long likeCountThisWeek;

    private Role role;

    private Boolean isLocked;
}
