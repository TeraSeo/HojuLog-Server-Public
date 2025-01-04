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

    @GetMapping("get/pageable/recent/property")
    public ResponseEntity<PropertyPostPaginationResponse> getRecentPageablePropertyPosts(@RequestParam int page, @RequestParam int size) {
        Page<PropertyPost> posts = propertyPostService.getCreatedAtDescPostsByPage(PageRequest.of(page - 1, size));
        List<NormalPropertyPostDto> postDtoList = posts.getContent()
                .stream()
                .map(post -> post.convertPostToNormalPropertyPostDto())
                .collect(Collectors.toList());

        PropertyPostPaginationResponse postPaginationResponse = PropertyPostPaginationResponse.builder().pageSize(posts.getTotalPages()).currentPagePostsCount(posts.getNumberOfElements()).currentPage(page).posts(postDtoList).build();
        return ResponseEntity.ok(postPaginationResponse);
    }

    @GetMapping("get/pageable/recent/job")
    public ResponseEntity<JobPostPaginationResponse> getRecentPageableJobPosts(@RequestParam int page, @RequestParam int size) {
        Page<JobPost> posts = jobPostService.getCreatedAtDescPostsByPage(PageRequest.of(page - 1, size));
        List<NormalJobPostDto> postDtoList = posts.getContent()
                .stream()
                .map(post -> post.convertPostToNormalJobPostDto())
                .collect(Collectors.toList());

        JobPostPaginationResponse postPaginationResponse = JobPostPaginationResponse.builder().pageSize(posts.getTotalPages()).currentPagePostsCount(posts.getNumberOfElements()).currentPage(page).posts(postDtoList).build();
        return ResponseEntity.ok(postPaginationResponse);
    }

    @GetMapping("get/pageable/recent/transaction")
    public ResponseEntity<TransactionPostPaginationResponse> getRecentPageableTransactionPosts(@RequestParam int page, @RequestParam int size) {
        Page<TransactionPost> posts = transactionPostService.getCreatedAtDescPostsByPage(PageRequest.of(page - 1, size));
        List<NormalTransactionPostDto> postDtoList = posts.getContent()
                .stream()
                .map(post -> post.convertPostToNormalTransactionPostDto())
                .collect(Collectors.toList());

        TransactionPostPaginationResponse postPaginationResponse = TransactionPostPaginationResponse.builder().pageSize(posts.getTotalPages()).currentPagePostsCount(posts.getNumberOfElements()).currentPage(page).posts(postDtoList).build();
        return ResponseEntity.ok(postPaginationResponse);
    }

    @GetMapping("get/pageable/recent/society")
    public ResponseEntity<SocietyPostPaginationResponse> getRecentPageableSocietyPosts(@RequestParam int page, @RequestParam int size) {
        Page<SocietyPost> posts = societyPostService.getCreatedAtDescPostsByPage(PageRequest.of(page - 1, size));
        List<NormalSocietyPostDto> postDtoList = posts.getContent()
                .stream()
                .map(post -> post.convertPostToNormalSocietyPostDto())
                .collect(Collectors.toList());

        SocietyPostPaginationResponse postPaginationResponse = SocietyPostPaginationResponse.builder().pageSize(posts.getTotalPages()).currentPagePostsCount(posts.getNumberOfElements()).currentPage(page).posts(postDtoList).build();
        return ResponseEntity.ok(postPaginationResponse);
    }

    @GetMapping("get/pageable/recent/travel")
    public ResponseEntity<TravelPostPaginationResponse> getRecentPageableTravelPosts(@RequestParam int page, @RequestParam int size) {
        Page<TravelPost> posts = travelPostService.getCreatedAtDescPostsByPage(PageRequest.of(page - 1, size));
        List<NormalTravelPostDto> postDtoList = posts.getContent()
                .stream()
                .map(post -> post.convertPostToNormalTravelPostDto())
                .collect(Collectors.toList());

        TravelPostPaginationResponse postPaginationResponse = TravelPostPaginationResponse.builder().pageSize(posts.getTotalPages()).currentPagePostsCount(posts.getNumberOfElements()).currentPage(page).posts(postDtoList).build();
        return ResponseEntity.ok(postPaginationResponse);
    }

    @GetMapping("get/pageable/recent/study")
    public ResponseEntity<StudyPostPaginationResponse> getRecentPageableStudyPosts(@RequestParam int page, @RequestParam int size) {
        Page<StudyPost> posts = studyPostService.getCreatedAtDescPostsByPage(PageRequest.of(page - 1, size));
        List<NormalStudyPostDto> postDtoList = posts.getContent()
                .stream()
                .map(post -> post.convertPostToNormalStudyPostDto())
                .collect(Collectors.toList());

        StudyPostPaginationResponse postPaginationResponse = StudyPostPaginationResponse.builder().pageSize(posts.getTotalPages()).currentPagePostsCount(posts.getNumberOfElements()).currentPage(page).posts(postDtoList).build();
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

    @GetMapping("get/specific/property")
    public ResponseEntity<DetailedPropertyPostDto> getSpecificPropertyPost(@RequestParam Long postId, @RequestParam Long userId) {
        PropertyPost propertyPost = propertyPostService.getPostById(postId);
        DetailedPropertyPostDto detailedPropertyPostDto = propertyPost.convertPostToDetailedPropertyPostDto(userId);
        postService.addViewCount(postId);
        return ResponseEntity.ok(detailedPropertyPostDto);
    }

    @GetMapping("get/specific/job")
    public ResponseEntity<DetailedJobPostDto> getSpecificJobPost(@RequestParam Long postId, @RequestParam Long userId) {
        JobPost jobPost = jobPostService.getPostById(postId);
        DetailedJobPostDto detailedJobPostDto = jobPost.convertPostToDetailedJobPostDto(userId);
        postService.addViewCount(postId);
        return ResponseEntity.ok(detailedJobPostDto);
    }

    @GetMapping("get/specific/transaction")
    public ResponseEntity<DetailedTransactionPostDto> getSpecificTransactionPost(@RequestParam Long postId, @RequestParam Long userId) {
        TransactionPost transactionPost = transactionPostService.getPostById(postId);
        DetailedTransactionPostDto detailedTransactionPostDto = transactionPost.convertPostToDetailedTransactionPostDto(userId);
        postService.addViewCount(postId);
        return ResponseEntity.ok(detailedTransactionPostDto);
    }

    @GetMapping("get/specific/society")
    public ResponseEntity<DetailedSocietyPostDto> getSpecificSocietyPost(@RequestParam Long postId, @RequestParam Long userId) {
        SocietyPost societyPost = societyPostService.getPostById(postId);
        DetailedSocietyPostDto detailedSocietyPostDto = societyPost.convertPostToDetailedSocietyPostDto(userId);
        postService.addViewCount(postId);
        return ResponseEntity.ok(detailedSocietyPostDto);
    }

    @GetMapping("get/specific/travel")
    public ResponseEntity<DetailedTravelPostDto> getSpecificTravelPost(@RequestParam Long postId, @RequestParam Long userId) {
        TravelPost travelPost = travelPostService.getPostById(postId);
        DetailedTravelPostDto detailedTravelPostDto = travelPost.convertPostToDetailedTravelPostDto(userId);
        postService.addViewCount(postId);
        return ResponseEntity.ok(detailedTravelPostDto);
    }

    @GetMapping("get/specific/study")
    public ResponseEntity<DetailedStudyPostDto> getSpecificStudyPost(@RequestParam Long postId, @RequestParam Long userId) {
        StudyPost studyPost = studyPostService.getPostById(postId);
        DetailedStudyPostDto detailedStudyPostDto = studyPost.convertPostToDetailedStudyPostDto(userId);
        postService.addViewCount(postId);
        return ResponseEntity.ok(detailedStudyPostDto);
    }
}
