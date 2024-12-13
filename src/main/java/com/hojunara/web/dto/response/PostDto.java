package com.hojunara.web.dto.response;

import com.hojunara.web.entity.*;
import lombok.*;

import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class PostDto {

    private Long postId;

    private String title;

    private String description;

    private Category category;

    private SubCategory subCategory;

    private String contact;

    private String email;

    private Boolean isPortrait;

    private List<String> imageUrls;

    private Period period;

    private Long price;

    private Long userId;

    private Timestamp createdAt;
}
