package com.promo.web.entity;

import com.promo.web.dto.response.PostDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostPaginationResponse {
    private int pageSize;
    private int currentPage;
    private int currentPagePostsCount;
    private List<PostDto> posts;
}
