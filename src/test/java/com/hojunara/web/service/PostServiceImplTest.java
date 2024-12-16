package com.hojunara.web.service;

import com.hojunara.web.dto.request.UserDto;
import com.hojunara.web.entity.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
class PostServiceImplTest {
    @Autowired
    private PostService postService;

    @Test
    void getRecent5PostsByCategory() {
        List<Post> recentTop5PostsByCategory = postService.getRecent5PostsByCategory(Category.부동산);
        assertEquals(recentTop5PostsByCategory.size(), 5);
    }

    @Test
    void getPostsLatestPagination() {
        Page<Post> posts = postService.getPostsByPageNCondition(PageRequest.of(0, 2), "Latest");
        assertEquals(posts.getNumberOfElements(), 2);
    }

}