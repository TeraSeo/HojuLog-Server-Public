package com.hojunara.web.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SocietyPostPaginationResponse {
    private int pageSize;
    private int currentPage;
    private int currentPagePostsCount;
    private List<NormalSocietyPostDto> posts;
}
