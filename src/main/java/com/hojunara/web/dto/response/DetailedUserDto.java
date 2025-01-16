package com.hojunara.web.dto.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class DetailedUserDto {

    private Long id;

    private String username;

    private String description;

    private String profilePicture;

    private List<Long> uploadedPostIds;
}
