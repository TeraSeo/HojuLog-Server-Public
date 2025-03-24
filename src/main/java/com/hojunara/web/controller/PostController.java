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
    private final PinnablePostService pinnablePostService;
    private final WorldCupPostService worldCupPostService;
    private final CandidateService candidateService;

    @Autowired
    public PostController(PostService postService, PropertyPostService propertyPostService, JobPostService jobPostService, TransactionPostService transactionPostService, SocietyPostService societyPostService, TravelPostService travelPostService, StudyPostService studyPostService, PinnablePostService pinnablePostService, WorldCupPostService worldCupPostService, CandidateService candidateService) {
        this.postService = postService;
        this.propertyPostService = propertyPostService;
        this.jobPostService = jobPostService;
        this.transactionPostService = transactionPostService;
        this.societyPostService = societyPostService;
        this.travelPostService = travelPostService;
        this.studyPostService = studyPostService;
        this.pinnablePostService = pinnablePostService;
        this.worldCupPostService = worldCupPostService;
        this.candidateService = candidateService;
    }

    @PostMapping("create/property")
    public ResponseEntity<Boolean> createPropertyPost(
            @Valid @RequestPart PropertyPostDto propertyPostDto,
            @RequestPart(required = false) MultipartFile[] images
    ) {
        Post post = propertyPostService.createPost(propertyPostDto, images);
        return ResponseEntity.ok(post != null);
    }

    @PutMapping("update/property")
    public ResponseEntity<Boolean> updatePropertyPost(
            @Valid @RequestPart UpdatePropertyPostDto updatePropertyPostDto,
            @RequestPart(required = false) MultipartFile[] images
    ) {
        Post post = propertyPostService.updatePost(updatePropertyPostDto, images);
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

    @PutMapping("update/job")
    public ResponseEntity<Boolean> updateJobPost(
            @Valid @RequestPart UpdateJobPostDto updateJobPostDto,
            @RequestPart(required = false) MultipartFile[] images
    ) {
        Post post = jobPostService.updatePost(updateJobPostDto, images);
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

    @PutMapping("update/transaction")
    public ResponseEntity<Boolean> updateTransactionPost(
            @Valid @RequestPart UpdateTransactionPostDto updateTransactionPostDto,
            @RequestPart(required = false) MultipartFile[] images
    ) {
        Post post = transactionPostService.updatePost(updateTransactionPostDto, images);
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

    @PutMapping("update/worldcup")
    public ResponseEntity<Boolean> updateWorldCupPost(
            @Valid @RequestPart UpdateWorldCupPostDto updateWorldCupPostDto,
            @RequestPart(required = false) MultipartFile[] images,
            @RequestPart(required = false) MultipartFile coverImage
    ) {
        Post post = worldCupPostService.updatePost(updateWorldCupPostDto, images, coverImage);
        return ResponseEntity.ok(post != null);
    }

    @PostMapping("create/worldcup")
    public ResponseEntity<Boolean> createWorldCupPost(
            @Valid @RequestPart WorldCupPostDto worldCupPostDto,
            @RequestPart(required = false) MultipartFile[] images,
            @RequestPart(required = false) MultipartFile coverImage
    ) {
        WorldCupPost post = worldCupPostService.createWorldCupPost(worldCupPostDto, images, coverImage);
        return ResponseEntity.ok(post != null);
    }

    @PutMapping("update/society")
    public ResponseEntity<Boolean> updateSocietyPost(
            @Valid @RequestPart UpdateSocietyPostDto updateSocietyPostDto,
            @RequestPart(required = false) MultipartFile[] images
    ) {
        Post post = societyPostService.updatePost(updateSocietyPostDto, images);
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

    @PutMapping("update/travel")
    public ResponseEntity<Boolean> updateTravelPost(
            @Valid @RequestPart UpdateTravelPostDto updateTravelPostDto,
            @RequestPart(required = false) MultipartFile[] images
    ) {
        Post post = travelPostService.updatePost(updateTravelPostDto, images);
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

    @PutMapping("update/study")
    public ResponseEntity<Boolean> updateStudyPost(
            @Valid @RequestPart UpdateStudyPostDto updateStudyPostDto,
            @RequestPart(required = false) MultipartFile[] images
    ) {
        Post post = studyPostService.updatePost(updateStudyPostDto, images);
        return ResponseEntity.ok(post != null);
    }

    @GetMapping("get/pageable/recent/worldcup")
    public ResponseEntity<WorldCupPostPaginationResponse> getRecentPageableWorldCupPosts(@RequestParam int page, @RequestParam int size) {
        Page<WorldCupPost> posts = worldCupPostService.getCreatedAtDescPostsByPage(PageRequest.of(page - 1, size));
        List<NormalWorldCupPostDto> postDtoList = posts.getContent()
                .stream()
                .map(post -> post.convertPostToNormalCupPostDto())
                .collect(Collectors.toList());

        WorldCupPostPaginationResponse postPaginationResponse = WorldCupPostPaginationResponse.builder().pageSize(posts.getTotalPages()).currentPagePostsCount(posts.getNumberOfElements()).currentPage(page).posts(postDtoList).build();
        return ResponseEntity.ok(postPaginationResponse);
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

    @GetMapping("get/pageable/worldcup/subcategory")
    public ResponseEntity<WorldCupPostPaginationResponse> getRecentPageableWorldCupPosts(@RequestParam int page, @RequestParam int size, @RequestParam SubCategory subCategory) {
        Page<WorldCupPost> posts = worldCupPostService.getCreatedAtDescPostsByPageNSubCategory(subCategory, PageRequest.of(page - 1, size));
        List<NormalWorldCupPostDto> postDtoList = posts.getContent()
                .stream()
                .map(post -> post.convertPostToNormalCupPostDto())
                .collect(Collectors.toList());

        WorldCupPostPaginationResponse postPaginationResponse = WorldCupPostPaginationResponse.builder().pageSize(posts.getTotalPages()).currentPagePostsCount(posts.getNumberOfElements()).currentPage(page).posts(postDtoList).build();
        return ResponseEntity.ok(postPaginationResponse);
    }

    @GetMapping("get/pageable/property/subcategory")
    public ResponseEntity<PropertyPostPaginationResponse> getRecentPageablePropertyPosts(@RequestParam int page, @RequestParam int size, @RequestParam SubCategory subCategory) {
        Page<PropertyPost> posts = propertyPostService.getCreatedAtDescPostsByPageNSubCategory(subCategory, PageRequest.of(page - 1, size));
        List<NormalPropertyPostDto> postDtoList = posts.getContent()
                .stream()
                .map(post -> post.convertPostToNormalPropertyPostDto())
                .collect(Collectors.toList());

        PropertyPostPaginationResponse postPaginationResponse = PropertyPostPaginationResponse.builder().pageSize(posts.getTotalPages()).currentPagePostsCount(posts.getNumberOfElements()).currentPage(page).posts(postDtoList).build();
        return ResponseEntity.ok(postPaginationResponse);
    }

    @GetMapping("get/pageable/job/subcategory")
    public ResponseEntity<JobPostPaginationResponse> getRecentPageableJobPosts(@RequestParam int page, @RequestParam int size, @RequestParam SubCategory subCategory) {
        Page<JobPost> posts = jobPostService.getCreatedAtDescPostsByPageNSubCategory(subCategory, PageRequest.of(page - 1, size));
        List<NormalJobPostDto> postDtoList = posts.getContent()
                .stream()
                .map(post -> post.convertPostToNormalJobPostDto())
                .collect(Collectors.toList());

        JobPostPaginationResponse postPaginationResponse = JobPostPaginationResponse.builder().pageSize(posts.getTotalPages()).currentPagePostsCount(posts.getNumberOfElements()).currentPage(page).posts(postDtoList).build();
        return ResponseEntity.ok(postPaginationResponse);
    }

    @GetMapping("get/pageable/transaction/subcategory")
    public ResponseEntity<TransactionPostPaginationResponse> getRecentPageableTransactionPosts(@RequestParam int page, @RequestParam int size, @RequestParam SubCategory subCategory) {
        Page<TransactionPost> posts = transactionPostService.getCreatedAtDescPostsByPageNSubCategory(subCategory, PageRequest.of(page - 1, size));
        List<NormalTransactionPostDto> postDtoList = posts.getContent()
                .stream()
                .map(post -> post.convertPostToNormalTransactionPostDto())
                .collect(Collectors.toList());

        TransactionPostPaginationResponse postPaginationResponse = TransactionPostPaginationResponse.builder().pageSize(posts.getTotalPages()).currentPagePostsCount(posts.getNumberOfElements()).currentPage(page).posts(postDtoList).build();
        return ResponseEntity.ok(postPaginationResponse);
    }

    @GetMapping("get/pageable/society/subcategory")
    public ResponseEntity<SocietyPostPaginationResponse> getRecentPageableSocietyPosts(@RequestParam int page, @RequestParam int size, @RequestParam SubCategory subCategory) {
        Page<SocietyPost> posts = societyPostService.getCreatedAtDescPostsByPageNSubCategory(subCategory, PageRequest.of(page - 1, size));
        List<NormalSocietyPostDto> postDtoList = posts.getContent()
                .stream()
                .map(post -> post.convertPostToNormalSocietyPostDto())
                .collect(Collectors.toList());

        SocietyPostPaginationResponse postPaginationResponse = SocietyPostPaginationResponse.builder().pageSize(posts.getTotalPages()).currentPagePostsCount(posts.getNumberOfElements()).currentPage(page).posts(postDtoList).build();
        return ResponseEntity.ok(postPaginationResponse);
    }

    @GetMapping("get/pageable/travel/subcategory")
    public ResponseEntity<TravelPostPaginationResponse> getRecentPageableTravelPosts(@RequestParam int page, @RequestParam int size, @RequestParam SubCategory subCategory) {
        Page<TravelPost> posts = travelPostService.getCreatedAtDescPostsByPageNSubCategory(subCategory, PageRequest.of(page - 1, size));
        List<NormalTravelPostDto> postDtoList = posts.getContent()
                .stream()
                .map(post -> post.convertPostToNormalTravelPostDto())
                .collect(Collectors.toList());

        TravelPostPaginationResponse postPaginationResponse = TravelPostPaginationResponse.builder().pageSize(posts.getTotalPages()).currentPagePostsCount(posts.getNumberOfElements()).currentPage(page).posts(postDtoList).build();
        return ResponseEntity.ok(postPaginationResponse);
    }

    @GetMapping("get/pageable/study/subcategory")
    public ResponseEntity<StudyPostPaginationResponse> getRecentPageableStudyPosts(@RequestParam int page, @RequestParam int size, @RequestParam SubCategory subCategory) {
        Page<StudyPost> posts = studyPostService.getCreatedAtDescPostsByPageNSubCategory(subCategory, PageRequest.of(page - 1, size));
        List<NormalStudyPostDto> postDtoList = posts.getContent()
                .stream()
                .map(post -> post.convertPostToNormalStudyPostDto())
                .collect(Collectors.toList());

        StudyPostPaginationResponse postPaginationResponse = StudyPostPaginationResponse.builder().pageSize(posts.getTotalPages()).currentPagePostsCount(posts.getNumberOfElements()).currentPage(page).posts(postDtoList).build();
        return ResponseEntity.ok(postPaginationResponse);
    }

    @GetMapping("get/pageable/own/posts")
    public ResponseEntity<WholePostPaginationResponse> getPageableOwnPosts(@RequestHeader int userId, @RequestParam int page, @RequestParam int size) {
        Page<Post> posts = postService.getPostsByPageNUser(Long.valueOf(userId), PageRequest.of(page - 1, size));
        List<SummarizedPostDto> summarizedPostDtoList = posts.getContent()
                .stream()
                .map(post -> post.convertToSummarizedPostDto())
                .collect(Collectors.toList());

        WholePostPaginationResponse postPaginationResponse = WholePostPaginationResponse.builder().pageSize(posts.getTotalPages()).currentPagePostsCount(posts.getNumberOfElements()).currentPage(page).posts(summarizedPostDtoList).build();
        return ResponseEntity.ok(postPaginationResponse);
    }

    @GetMapping("get/pageable/others/posts")
    public ResponseEntity<WholePostPaginationResponse> getPageableOthersPosts(@RequestHeader int userId, @RequestParam int page, @RequestParam int size) {
        Page<Post> posts = postService.getPostsByPageNUser(Long.valueOf(userId), PageRequest.of(page - 1, size));
        List<SummarizedPostDto> summarizedPostDtoList = posts.getContent()
                .stream()
                .map(post -> post.convertToSummarizedPostDto())
                .collect(Collectors.toList());

        WholePostPaginationResponse postPaginationResponse = WholePostPaginationResponse.builder().pageSize(posts.getTotalPages()).currentPagePostsCount(posts.getNumberOfElements()).currentPage(page).posts(summarizedPostDtoList).build();
        return ResponseEntity.ok(postPaginationResponse);
    }

    @GetMapping("get/pageable/liked/posts")
    public ResponseEntity<WholePostPaginationResponse> getPageableLikedPosts(@RequestHeader int userId, @RequestParam int page, @RequestParam int size) {
        Page<Post> posts = postService.getPostsByPageNLiked(Long.valueOf(userId), PageRequest.of(page - 1, size));
        List<SummarizedPostDto> summarizedPostDtoList = posts.getContent()
                .stream()
                .map(post -> post.convertToSummarizedPostDto())
                .collect(Collectors.toList());

        WholePostPaginationResponse postPaginationResponse = WholePostPaginationResponse.builder().pageSize(posts.getTotalPages()).currentPagePostsCount(posts.getNumberOfElements()).currentPage(page).posts(summarizedPostDtoList).build();
        return ResponseEntity.ok(postPaginationResponse);
    }

    @GetMapping("get/recent-5/worldcup/post")
    public ResponseEntity<List<SummarizedWorldCupPostDto>> getRecent5WorldCupPost() {
        List<WorldCupPost> worldCupPostList = worldCupPostService.getRecent5Posts();
        List<SummarizedWorldCupPostDto> summarizedWorldCupPostDtoList = worldCupPostList
                .stream()
                .map(post -> post.convertPostToSummarizedWorldCupPostDto())
                .collect(Collectors.toList());

        return ResponseEntity.ok(summarizedWorldCupPostDtoList);
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

    @GetMapping("get/specific/candidateDtoList")
    public ResponseEntity<List<CandidateDto>> getSpecificCandidateDtoList(@RequestParam Long postId) {
        WorldCupPost worldCupPost = worldCupPostService.getPostById(postId);
        List<Candidate> candidates = worldCupPost.getCandidates();
        List<CandidateDto> candidateDtoList = candidates.stream().map(candidate -> candidate.convertToCandidateDto()).collect(Collectors.toList());
        return ResponseEntity.ok(candidateDtoList);
    }

    @GetMapping("get/specific/worldcup")
    public ResponseEntity<DetailedWorldCupPostDto> getSpecificWorldCupPost(@RequestParam Long postId, @RequestHeader String userId) {
        WorldCupPost worldCupPost = worldCupPostService.getPostById(postId);
        DetailedWorldCupPostDto detailedPropertyPostDto = worldCupPost.convertPostToDetailedWorldCupPostDto(userId);
        postService.addViewCount(postId);
        return ResponseEntity.ok(detailedPropertyPostDto);
    }

    @GetMapping("get/specific/update/worldcup")
    public ResponseEntity<UpdateDetailedWorldCupPostDto> getSpecificWorldCupPost(@RequestParam Long postId) {
        WorldCupPost worldCupPost = worldCupPostService.getPostById(postId);
        UpdateDetailedWorldCupPostDto updateDetailedWorldCupPostDto = worldCupPost.convertPostToUpdatePostDto();
        return ResponseEntity.ok(updateDetailedWorldCupPostDto);
    }

    @GetMapping("get/specific/property")
    public ResponseEntity<DetailedPropertyPostDto> getSpecificPropertyPost(@RequestParam Long postId, @RequestHeader String userId) {
        PropertyPost propertyPost = propertyPostService.getPostById(postId);
        DetailedPropertyPostDto detailedPropertyPostDto = propertyPost.convertPostToDetailedPropertyPostDto(userId);
        postService.addViewCount(postId);

        return ResponseEntity.ok(detailedPropertyPostDto);
    }

    @GetMapping("get/specific/job")
    public ResponseEntity<DetailedJobPostDto> getSpecificJobPost(@RequestParam Long postId, @RequestHeader String userId) {
        JobPost jobPost = jobPostService.getPostById(postId);
        DetailedJobPostDto detailedJobPostDto = jobPost.convertPostToDetailedJobPostDto(userId);
        postService.addViewCount(postId);

        return ResponseEntity.ok(detailedJobPostDto);
    }

    @GetMapping("get/specific/transaction")
    public ResponseEntity<DetailedTransactionPostDto> getSpecificTransactionPost(@RequestParam Long postId, @RequestHeader String userId) {
        TransactionPost transactionPost = transactionPostService.getPostById(postId);
        DetailedTransactionPostDto detailedTransactionPostDto = transactionPost.convertPostToDetailedTransactionPostDto(userId);
        postService.addViewCount(postId);
        return ResponseEntity.ok(detailedTransactionPostDto);
    }

    @GetMapping("get/specific/society")
    public ResponseEntity<DetailedSocietyPostDto> getSpecificSocietyPost(@RequestParam Long postId, @RequestHeader String userId) {
        SocietyPost societyPost = societyPostService.getPostById(postId);
        DetailedSocietyPostDto detailedSocietyPostDto = societyPost.convertPostToDetailedSocietyPostDto(userId);
        postService.addViewCount(postId);
        return ResponseEntity.ok(detailedSocietyPostDto);
    }

    @GetMapping("get/specific/travel")
    public ResponseEntity<DetailedTravelPostDto> getSpecificTravelPost(@RequestParam Long postId, @RequestHeader String userId) {
        TravelPost travelPost = travelPostService.getPostById(postId);
        DetailedTravelPostDto detailedTravelPostDto = travelPost.convertPostToDetailedTravelPostDto(userId);
        postService.addViewCount(postId);

        return ResponseEntity.ok(detailedTravelPostDto);
    }

    @GetMapping("get/update/propertyDto")
    public ResponseEntity<UpdatePropertyPostDto> getUpdatePropertyPostDto(@RequestParam Long postId) {
        PropertyPost propertyPost = propertyPostService.getPostById(postId);
        UpdatePropertyPostDto updateTravelPostDto = propertyPost.convertToUpdatePropertyPostDto();
        return ResponseEntity.ok(updateTravelPostDto);
    }

    @GetMapping("get/update/jobDto")
    public ResponseEntity<UpdateJobPostDto> getUpdateJobPostDto(@RequestParam Long postId) {
        JobPost jobPost = jobPostService.getPostById(postId);
        UpdateJobPostDto updateJobPostDto = jobPost.convertToUpdateJobPostDto();
        return ResponseEntity.ok(updateJobPostDto);
    }

    @GetMapping("get/update/transactionDto")
    public ResponseEntity<UpdateTransactionPostDto> getUpdateTransactionPostDto(@RequestParam Long postId) {
        TransactionPost transactionPost = transactionPostService.getPostById(postId);
        UpdateTransactionPostDto updateTransactionPostDto = transactionPost.convertToUpdateTransactionPostDto();
        return ResponseEntity.ok(updateTransactionPostDto);
    }

    @GetMapping("get/update/travelDto")
    public ResponseEntity<UpdateTravelPostDto> getUpdateTravelPostDto(@RequestParam Long postId) {
        TravelPost travelPost = travelPostService.getPostById(postId);
        UpdateTravelPostDto travelPostDto = travelPost.convertToUpdateTravelPostDto();
        return ResponseEntity.ok(travelPostDto);
    }

    @GetMapping("get/update/studyDto")
    public ResponseEntity<UpdateStudyPostDto> getUpdateStudyPostDto(@RequestParam Long postId) {
        StudyPost studyPost = studyPostService.getPostById(postId);
        UpdateStudyPostDto studyPostDto = studyPost.convertToUpdateStudyPostDto();
        return ResponseEntity.ok(studyPostDto);
    }

    @GetMapping("get/update/societyDto")
    public ResponseEntity<UpdateSocietyPostDto> getUpdateSocietyPostDto(@RequestParam Long postId) {
        SocietyPost societyPost = societyPostService.getPostById(postId);
        UpdateSocietyPostDto societyPostDto = societyPost.convertToUpdateSocietyPostDto();
        return ResponseEntity.ok(societyPostDto);
    }

    @GetMapping("get/specific/study")
    public ResponseEntity<DetailedStudyPostDto> getSpecificStudyPost(@RequestParam Long postId, @RequestHeader String userId) {
        StudyPost studyPost = studyPostService.getPostById(postId);
        DetailedStudyPostDto detailedStudyPostDto = studyPost.convertPostToDetailedStudyPostDto(userId);
        postService.addViewCount(postId);

        return ResponseEntity.ok(detailedStudyPostDto);
    }

    @GetMapping("get/summarised/specific/post")
    public ResponseEntity<SummarizedPostDto> getSummarizedOwnPost(@RequestParam Long postId) {
        PinnablePost post = pinnablePostService.getPostById(postId);
        SummarizedPostDto summarizedPostDto = post.convertToSummarizedPostDto();
        return ResponseEntity.ok(summarizedPostDto);
    }

    @GetMapping("get/worldcup/by/search/option")
    public ResponseEntity<WorldCupPostPaginationResponse> searchWorldCup(@RequestParam String title, @RequestParam String subCategory, @RequestParam String suburb, @RequestParam(required = false) List<String> keywords, @RequestParam int page, @RequestParam int size) {
        List<WorldCupPost> searchedPosts = worldCupPostService.searchWorldCupPost(title, subCategory, keywords);
        Page<WorldCupPost> posts = worldCupPostService.convertPostsAsPage(searchedPosts, PageRequest.of(page - 1, size));
        List<NormalWorldCupPostDto> postDtoList = posts.getContent()
                .stream()
                .map(post -> post.convertPostToNormalCupPostDto())
                .collect(Collectors.toList());

        WorldCupPostPaginationResponse postPaginationResponse = WorldCupPostPaginationResponse.builder().pageSize(posts.getTotalPages()).currentPagePostsCount(posts.getNumberOfElements()).currentPage(page).posts(postDtoList).build();
        return ResponseEntity.ok(postPaginationResponse);
    }

    @GetMapping("get/property/by/search/option")
    public ResponseEntity<PropertyPostPaginationResponse> searchProperty(@RequestParam String title, @RequestParam String subCategory, @RequestParam String suburb, @RequestParam(required = false) List<String> keywords, @RequestParam int page, @RequestParam int size) {
        List<PropertyPost> searchedPosts = propertyPostService.searchPropertyPost(title, subCategory, suburb, keywords);
        Page<PropertyPost> posts = propertyPostService.convertPostsAsPage(searchedPosts, PageRequest.of(page - 1, size));
        List<NormalPropertyPostDto> postDtoList = posts.getContent()
                .stream()
                .map(post -> post.convertPostToNormalPropertyPostDto())
                .collect(Collectors.toList());

        PropertyPostPaginationResponse postPaginationResponse = PropertyPostPaginationResponse.builder().pageSize(posts.getTotalPages()).currentPagePostsCount(posts.getNumberOfElements()).currentPage(page).posts(postDtoList).build();
        return ResponseEntity.ok(postPaginationResponse);
    }

    @GetMapping("get/job/by/search/option")
    public ResponseEntity<JobPostPaginationResponse> searchJob(@RequestParam String title, @RequestParam String subCategory, @RequestParam String suburb, @RequestParam(required = false) List<String> keywords, @RequestParam int page, @RequestParam int size) {
        List<JobPost> searchedPosts = jobPostService.searchJobPost(title, subCategory, suburb, keywords);
        Page<JobPost> posts = jobPostService.convertPostsAsPage(searchedPosts, PageRequest.of(page - 1, size));
        List<NormalJobPostDto> postDtoList = posts.getContent()
                .stream()
                .map(post -> post.convertPostToNormalJobPostDto())
                .collect(Collectors.toList());

        JobPostPaginationResponse postPaginationResponse = JobPostPaginationResponse.builder().pageSize(posts.getTotalPages()).currentPagePostsCount(posts.getNumberOfElements()).currentPage(page).posts(postDtoList).build();
        return ResponseEntity.ok(postPaginationResponse);
    }

    @GetMapping("get/transaction/by/search/option")
    public ResponseEntity<TransactionPostPaginationResponse> searchTransaction(@RequestParam String title, @RequestParam String subCategory, @RequestParam String suburb, @RequestParam(required = false) List<String> keywords, @RequestParam int page, @RequestParam int size) {
        List<TransactionPost> searchedPosts = transactionPostService.searchTransactionPost(title, subCategory, suburb, keywords);
        Page<TransactionPost> posts = transactionPostService.convertPostsAsPage(searchedPosts, PageRequest.of(page - 1, size));
        List<NormalTransactionPostDto> postDtoList = posts.getContent()
                .stream()
                .map(post -> post.convertPostToNormalTransactionPostDto())
                .collect(Collectors.toList());

        TransactionPostPaginationResponse postPaginationResponse = TransactionPostPaginationResponse.builder().pageSize(posts.getTotalPages()).currentPagePostsCount(posts.getNumberOfElements()).currentPage(page).posts(postDtoList).build();
        return ResponseEntity.ok(postPaginationResponse);
    }

    @GetMapping("get/society/by/search/option")
    public ResponseEntity<SocietyPostPaginationResponse> searchSociety(@RequestParam String title, @RequestParam String subCategory, @RequestParam(required = false) List<String> keywords, @RequestParam int page, @RequestParam int size) {
        List<SocietyPost> searchedPosts = societyPostService.searchSocietyPost(title, subCategory, keywords);
        Page<SocietyPost> posts = societyPostService.convertPostsAsPage(searchedPosts, PageRequest.of(page - 1, size));
        List<NormalSocietyPostDto> postDtoList = posts.getContent()
                .stream()
                .map(post -> post.convertPostToNormalSocietyPostDto())
                .collect(Collectors.toList());

        SocietyPostPaginationResponse postPaginationResponse = SocietyPostPaginationResponse.builder().pageSize(posts.getTotalPages()).currentPagePostsCount(posts.getNumberOfElements()).currentPage(page).posts(postDtoList).build();
        return ResponseEntity.ok(postPaginationResponse);
    }

    @GetMapping("get/travel/by/search/option")
    public ResponseEntity<TravelPostPaginationResponse> searchTravel(@RequestParam String title, @RequestParam String subCategory, @RequestParam(required = false) List<String> keywords, @RequestParam int page, @RequestParam int size) {
        List<TravelPost> searchedPosts = travelPostService.searchTravelPost(title, subCategory, keywords);
        Page<TravelPost> posts = travelPostService.convertPostsAsPage(searchedPosts, PageRequest.of(page - 1, size));
        List<NormalTravelPostDto> postDtoList = posts.getContent()
                .stream()
                .map(post -> post.convertPostToNormalTravelPostDto())
                .collect(Collectors.toList());

        TravelPostPaginationResponse postPaginationResponse = TravelPostPaginationResponse.builder().pageSize(posts.getTotalPages()).currentPagePostsCount(posts.getNumberOfElements()).currentPage(page).posts(postDtoList).build();
        return ResponseEntity.ok(postPaginationResponse);
    }

    @GetMapping("get/study/by/search/option")
    public ResponseEntity<StudyPostPaginationResponse> searchStudy(@RequestParam String title, @RequestParam String subCategory, @RequestParam(required = false) List<String> keywords, @RequestParam int page, @RequestParam int size) {
        List<StudyPost> searchedPosts = studyPostService.searchStudyPost(title, subCategory, keywords);
        Page<StudyPost> posts = studyPostService.convertPostsAsPage(searchedPosts, PageRequest.of(page - 1, size));
        List<NormalStudyPostDto> postDtoList = posts.getContent()
                .stream()
                .map(post -> post.convertPostToNormalStudyPostDto())
                .collect(Collectors.toList());

        StudyPostPaginationResponse postPaginationResponse = StudyPostPaginationResponse.builder().pageSize(posts.getTotalPages()).currentPagePostsCount(posts.getNumberOfElements()).currentPage(page).posts(postDtoList).build();
        return ResponseEntity.ok(postPaginationResponse);
    }

    @DeleteMapping("delete/post")
    public ResponseEntity<Boolean> removePost(@RequestParam Long postId) {
        Boolean isDeleted = postService.removePost(postId);
        return ResponseEntity.ok(isDeleted);
    }

    @PutMapping("pin/post")
    public ResponseEntity<Boolean> pinPost(@RequestParam Long postId, @RequestParam Long userId) {
        Boolean isPinned = pinnablePostService.updatePinStatus(postId, userId);
        return ResponseEntity.ok(isPinned);
    }

    @PutMapping("add/victory")
    public ResponseEntity<Boolean> updateVictory(@RequestParam Long candidateId) {
        Boolean isUpdated = candidateService.updateVictory(candidateId);
        return ResponseEntity.ok(isUpdated);
    }
}