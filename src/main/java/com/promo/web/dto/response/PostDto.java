package com.promo.web.dto.response;

import com.promo.web.entity.*;
import lombok.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
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

    private String subTitle;

    private String description;

    private Category category;

    private SubCategory subCategory;

    private String playStoreUrl;

    private String appStoreUrl;

    private String webUrl;

    private String visibility;

    private Boolean isOwnWork;

    private String ownerEmail;

    private List<String> tags;

    private Boolean isPortrait;

    private LocalDateTime startDateTime;

    private LocalDateTime endDateTime;

    private String location;

    private String logoUrl;

    private List<String> imageUrls;

    private String youtubeUrl;

    private Long likedUserCount;

    private Long userId;

    private List<Long> recentLaunchedPostIds;

    private Timestamp createdAt;

    private Boolean isCurrentUserLiked;

    private Boolean isCurrentUserBookmarked;

    private Long wholeCommentsLength;
}
