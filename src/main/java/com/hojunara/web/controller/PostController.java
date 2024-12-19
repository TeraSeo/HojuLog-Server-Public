package com.hojunara.web.controller;

import com.hojunara.web.dto.request.*;
import com.hojunara.web.dto.response.*;
import com.hojunara.web.entity.*;
import com.hojunara.web.service.*;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/post")
@Slf4j
public class PostController {

    private final PostService postService;
    private final PropertyPostService propertyPostService;
    private final JobPostService jobPostService;
    private final TransactionPostService transactionPostService;
    private final SocietyPostService societyPostService;
    private final TravelPostService travelPostService;
    private final StudyPostService studyPostService;

    @Autowired
    public PostController(PostService postService, PropertyPostService propertyPostService, JobPostService jobPostService, TransactionPostService transactionPostService, SocietyPostService societyPostService, TravelPostService travelPostService, StudyPostService studyPostService) {
        this.postService = postService;
        this.propertyPostService = propertyPostService;
        this.jobPostService = jobPostService;
        this.transactionPostService = transactionPostService;
        this.societyPostService = societyPostService;
        this.travelPostService = travelPostService;
        this.studyPostService = studyPostService;
    }

    @PostMapping("create/property")
    public ResponseEntity<Boolean> createPropertyPost(
            @Valid @RequestPart PropertyPostDto propertyPostDto,
            @RequestPart(required = false) MultipartFile[] images
    ) {
        Post post = propertyPostService.createPost(propertyPostDto, images);
        return ResponseEntity.ok(post != null);
    }

    @PostMapping("create/job")
    public ResponseEntity<Boolean> createJobPost(
            @Valid @RequestPart JobPostDto jobPostDto,
            @RequestPart(required = false) MultipartFile[] images
    ) {
        Post post = jobPostService.createPost(jobPostDto, images);
        return ResponseEntity.ok(post != null);
    }

    @PostMapping("create/transaction")
    public ResponseEntity<Boolean> createTransactionPost(
            @Valid @RequestPart TransactionPostDto transactionPostDto,
            @RequestPart(required = false) MultipartFile[] images
    ) {
        Post post = transactionPostService.createPost(transactionPostDto, images);
        return ResponseEntity.ok(post != null);
    }

    @PostMapping("create/society")
    public ResponseEntity<Boolean> createSocietyPost(
            @Valid @RequestPart SocietyPostDto societyPostDto,
            @RequestPart(required = false) MultipartFile[] images
    ) {
        Post post = societyPostService.createPost(societyPostDto, images);
        return ResponseEntity.ok(post != null);
    }

    @PostMapping("create/travel")
    public ResponseEntity<Boolean> createTravelPost(
            @Valid @RequestPart TravelPostDto travelPostDto,
            @RequestPart(required = false) MultipartFile[] images
    ) {
        Post post = travelPostService.createPost(travelPostDto, images);
        return ResponseEntity.ok(post != null);
    }

    @PostMapping("create/study")
    public ResponseEntity<Boolean> createStudyPost(
            @Valid @RequestPart StudyPostDto studyPostDto,
            @RequestPart(required = false) MultipartFile[] images
    ) {
        Post post = studyPostService.createPost(studyPostDto, images);
        return ResponseEntity.ok(post != null);
    }

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

    @GetMapping("get/recent-5/property/post")
    public ResponseEntity<List<SummarizedPropertyPostDto>> getRecent5PropertyPost() {
        List<PropertyPost> propertyPostList = propertyPostService.getRecent5Posts();
        List<SummarizedPropertyPostDto> summarizedPropertyPostDtoList = propertyPostList
                .stream()
                .map(post -> post.convertPostToSummarizedPropertyPostDto())
                .collect(Collectors.toList());

        return ResponseEntity.ok(summarizedPropertyPostDtoList);
    }

    @GetMapping("get/recent-5/job/post")
    public ResponseEntity<List<SummarizedJobPostDto>> getRecent5JobPost() {
        List<JobPost> jobPostList = jobPostService.getRecent5Posts();
        List<SummarizedJobPostDto> summarizedJobPostDtoList = jobPostList
                .stream()
                .map(post -> post.convertPostToSummarizedJobPostDto())
                .collect(Collectors.toList());

        return ResponseEntity.ok(summarizedJobPostDtoList);
    }

    @GetMapping("get/recent-5/transaction/post")
    public ResponseEntity<List<SummarizedTransactionPostDto>> getRecent5TransactionPost() {
        List<TransactionPost> transactionPostList = transactionPostService.getRecent5Posts();
        List<SummarizedTransactionPostDto> summarizedTransactionPostDtoList = transactionPostList
                .stream()
                .map(post -> post.convertPostToSummarizedTransactionPostDto())
                .collect(Collectors.toList());

        return ResponseEntity.ok(summarizedTransactionPostDtoList);
    }

    @GetMapping("get/recent-5/society/post")
    public ResponseEntity<List<SummarizedSocietyPostDto>> getRecent5SocietyPost() {
        List<SocietyPost> societyPostList = societyPostService.getRecent5Posts();
        List<SummarizedSocietyPostDto> summarizedSocietyPostDtoList = societyPostList
                .stream()
                .map(post -> post.convertPostToSummarizedSocietyPostDto())
                .collect(Collectors.toList());

        return ResponseEntity.ok(summarizedSocietyPostDtoList);
    }

    @GetMapping("get/recent-5/travel/post")
    public ResponseEntity<List<SummarizedTravelPostDto>> getRecent5TravelPost() {
        List<TravelPost> travelPostList = travelPostService.getRecent5Posts();
        List<SummarizedTravelPostDto> summarizedTravelPostDtoList = travelPostList
                .stream()
                .map(post -> post.convertPostToSummarizedTravelPostDto())
                .collect(Collectors.toList());

        return ResponseEntity.ok(summarizedTravelPostDtoList);
    }

    @GetMapping("get/recent-5/study/post")
    public ResponseEntity<List<SummarizedStudyPostDto>> getRecent5StudyPost() {
        List<StudyPost> studyPostList = studyPostService.getRecent5Posts();
        List<SummarizedStudyPostDto> summarizedStudyPostDtoList = studyPostList
                .stream()
                .map(post -> post.convertPostToSummarizedStudyPostDto())
                .collect(Collectors.toList());

        return ResponseEntity.ok(summarizedStudyPostDtoList);
    }

    @GetMapping("get/specific")
    public ResponseEntity<PostDto> getSpecificPost(@RequestParam Long postId, @RequestParam Long userId) {
        Post post = postService.getPostById(postId);
        PostDto postDto = post.convertToPostDto(userId);
        return ResponseEntity.ok(postDto);
    }

}
