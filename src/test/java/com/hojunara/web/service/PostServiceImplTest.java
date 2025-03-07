//package com.hojunara.web.service;
//
//import com.hojunara.web.dto.request.UserDto;
//import com.hojunara.web.dto.response.SummarizedPropertyPostDto;
//import com.hojunara.web.entity.*;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.mock.web.MockMultipartFile;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.nio.charset.StandardCharsets;
//import java.util.List;
//import java.util.stream.Collectors;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//@SpringBootTest
//@Transactional
//class PostServiceImplTest {
//    @Autowired
//    private PostService postService;
//
//    @Autowired
//    private PropertyPostService propertyPostService;
//
////    @Test
////    void getRecent5PostsByCategory() {
////        List<Post> recentTop5PostsByCategory = postService.getRecent5PostsByCategory(Category.부동산);
////        assertEquals(recentTop5PostsByCategory.size(), 5);
////    }
//
//    @Test
//    void getRecent5PropertyPostsB() {
//        List<PropertyPost> propertyPostList = propertyPostService.getRecent5Posts();
//        assertEquals(propertyPostList.size(), 5);
//    }
//
//    @Test
//    void getPostsLatestPagination() {
//        Page<Post> posts = postService.getPostsByPageNCondition(PageRequest.of(0, 2), "Latest");
//        assertEquals(posts.getNumberOfElements(), 2);
//    }
//
//    @Test
//    void getPropertyPostsLatestPagination() {
//        Page<PropertyPost> posts = propertyPostService.getCreatedAtDescPostsByPage(PageRequest.of(0, 10));
//        List<SummarizedPropertyPostDto> postDtoList = posts.getContent()
//                .stream()
//                .map(post -> post.convertPostToSummarizedPropertyPostDto())
//                .collect(Collectors.toList());
//        assertEquals(postDtoList.size(), 6);
//    }
//
//    @Test
//    void getPropertyPostsLatestPaginationNSubCategory() {
//        Page<PropertyPost> posts = propertyPostService.getCreatedAtDescPostsByPageNSubCategory(SubCategory.렌트, PageRequest.of(0, 10));
//        List<SummarizedPropertyPostDto> postDtoList = posts.getContent()
//                .stream()
//                .map(post -> post.convertPostToSummarizedPropertyPostDto())
//                .collect(Collectors.toList());
//        assertEquals(postDtoList.size(), 1);
//    }
//
//    @Test
//    void getPostsByUserLatestPagination() {
//        Page<Post> posts = postService.getPostsByPageNUser(1L ,PageRequest.of(0, 10));
//        List<Post> postList = posts.getContent();
//        assertEquals(postList.size(), 1);
//    }
//
//    @Test
//    void getLikedPostsByPagination() {
//        Page<Post> posts = postService.getPostsByPageNLiked(1L ,PageRequest.of(0, 10));
//        List<Post> postList = posts.getContent();
//        assertEquals(postList.size(), 1);
//    }
//}