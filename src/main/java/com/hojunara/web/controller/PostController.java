package com.hojunara.web.controller;

import com.hojunara.web.dto.response.PostDto;
import com.hojunara.web.entity.Post;
import com.hojunara.web.entity.PostPaginationResponse;
import com.hojunara.web.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/post")
@Slf4j
public class PostController {

    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

//    @PostMapping("create/technology")
//    public ResponseEntity<Boolean> createTechnologyPost(
//            @Valid @RequestPart TechnologyPostDto technologyPostDto,
//            @RequestPart(required = false) MultipartFile logoImage,
//            @RequestPart(required = false) MultipartFile[] images
//    ) {
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        String email = auth.getName();
//        Post post = technologyPostService.createPost(email, technologyPostDto, logoImage, images);
//        return ResponseEntity.ok(post != null);
//    }
//
//    @PostMapping("create/restaurant")
//    public ResponseEntity<Boolean> createRestaurantPost(
//            @Valid @RequestPart RestaurantPostDto restaurantPostDto,
//            @RequestPart(required = false) MultipartFile logoImage,
//            @RequestPart(required = false) MultipartFile[] images
//    ) {
//
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        String email = auth.getName();
//        Post post = restaurantPostService.createPost(email, restaurantPostDto, logoImage, images);
//        return ResponseEntity.ok(post != null);
//    }
//
//    @PostMapping("create/education")
//    public ResponseEntity<Boolean> createEducationPost(
//            @Valid @RequestPart EducationPostDto educationPostDto,
//            @RequestPart(required = false) MultipartFile logoImage,
//            @RequestPart(required = false) MultipartFile[] images
//    ) {
//
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        String email = auth.getName();
//        Post post = educationPostService.createPost(email, educationPostDto, logoImage, images);
//        return ResponseEntity.ok(post != null);
//    }
//
//    @PostMapping("create/lifestyle")
//    public ResponseEntity<Boolean> createLifeStylePost(
//            @Valid @RequestPart LifeStylePostDto lifeStylePostDto,
//            @RequestPart(required = false) MultipartFile logoImage,
//            @RequestPart(required = false) MultipartFile[] images
//    ) {
//
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        String email = auth.getName();
//        Post post = lifeStylePostService.createPost(email, lifeStylePostDto, logoImage, images);
//        return ResponseEntity.ok(post != null);
//    }
//
//    @PostMapping("create/entertainment")
//    public ResponseEntity<Boolean> createEntertainmentPost(
//            @Valid @RequestPart EntertainmentPostDto entertainmentPostDto,
//            @RequestPart(required = false) MultipartFile logoImage,
//            @RequestPart(required = false) MultipartFile[] images
//    ) {
//
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        String email = auth.getName();
//        Post post = entertainmentPostService.createPost(email, entertainmentPostDto, logoImage, images);
//        return ResponseEntity.ok(post != null);
//    }

    @GetMapping("get/whole-by-page-n-condition")
    public ResponseEntity<PostPaginationResponse> getPostsByCondition(@RequestParam int page, @RequestParam int size, @RequestParam String condition, @RequestParam Long userId) {
        Page<Post> posts = postService.getPostsByPageNCondition(PageRequest.of(page - 1, size), condition);
        List<PostDto> postDtoList = posts.getContent()
                .stream()
                .map(post -> post.convertToPostDto(userId))
                .collect(Collectors.toList());

        PostPaginationResponse postPaginationResponse = PostPaginationResponse.builder().pageSize(posts.getTotalPages()).currentPagePostsCount(posts.getNumberOfElements()).currentPage(page).posts(postDtoList).build();
        return ResponseEntity.ok(postPaginationResponse);
    }

    @GetMapping("get/specific")
    public ResponseEntity<PostDto> getSpecificPost(@RequestParam Long postId, @RequestParam Long userId) {
        Post post = postService.getPostById(postId);
        PostDto postDto = post.convertToPostDto(userId);
        return ResponseEntity.ok(postDto);
    }

}
