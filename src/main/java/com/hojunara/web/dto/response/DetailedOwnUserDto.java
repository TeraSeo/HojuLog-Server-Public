package com.hojunara.web.dto.response;

import com.hojunara.web.entity.Role;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class DetailedOwnUserDto {

    private Long id;

    private String username;

    private String description;

    private String profilePicture;

    private List<Long> uploadedPostIds;

    private List<Long> likedPostIds;

    private List<Long> requestedIds;

    private Role role;
}
