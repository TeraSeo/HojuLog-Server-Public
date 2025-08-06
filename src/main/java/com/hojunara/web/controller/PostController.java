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

/**
 * REST controller for managing all types of posts in the platform.
 * <p>
 * Provides endpoints for CRUD and search operations for posts including:
 * Article, Property, Job, Transaction, Society, Travel, Study, and World Cup posts.
 * Endpoints are prefixed with <code>/api/post</code>.
 * </p>
 *
 * Major responsibilities:
 * <ul>
 *     <li>Create, update, delete posts</li>
 *     <li>Paginated retrieval by type and filter</li>
 *     <li>Post detail retrieval</li>
 *     <li>Pinning and victory marking</li>
 * </ul>
 *
 * @author Taejun Seo
 */
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
    private final ArticlePostService articlePostService;

    @Autowired
    public PostController(PostService postService, PropertyPostService propertyPostService, JobPostService jobPostService, TransactionPostService transactionPostService, SocietyPostService societyPostService, TravelPostService travelPostService, StudyPostService studyPostService, PinnablePostService pinnablePostService, WorldCupPostService worldCupPostService, CandidateService candidateService, ArticlePostService articlePostService) {
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
        this.articlePostService = articlePostService;
    }

    /**
     * Updates an existing article post with optional images.
     *
     * @param updateArticlePostDto the updated article post data
     * @param images optional updated image files
     * @return {@code true} if update was successful
     */
    @PutMapping("update/article")
    public ResponseEntity<Boolean> updateArticle(
            @Valid @RequestPart UpdateArticlePostDto updateArticlePostDto,
            @RequestPart(required = false) MultipartFile[] images
    ) {
        Post post = articlePostService.updateArticle(updateArticlePostDto, images);
        return ResponseEntity.ok(post != null);
    }

    /**
     * Creates a new article post with optional images.
     *
     * @param articlePostDto the article post data
     * @param images optional image files
     * @return {@code true} if creation was successful
     */
    @PostMapping("create/article")
    public ResponseEntity<Boolean> createArticlePost(
            @Valid @RequestPart ArticlePostDto articlePostDto,
            @RequestPart(required = false) MultipartFile[] images
    ) {
        Post post = articlePostService.createArticle(articlePostDto, images);
        return ResponseEntity.ok(post != null);
    }

    /**
     * Creates a new property post with optional images.
     *
     * @param propertyPostDto the property post data
     * @param images optional image files
     * @return {@code true} if creation was successful
     */
    @PostMapping("create/property")
    public ResponseEntity<Boolean> createPropertyPost(
            @Valid @RequestPart PropertyPostDto propertyPostDto,
            @RequestPart(required = false) MultipartFile[] images
    ) {
        Post post = propertyPostService.createPost(propertyPostDto, images);
        return ResponseEntity.ok(post != null);
    }

    /**
     * Updates an existing property post with optional images.
     *
     * @param updatePropertyPostDto the updated property post data
     * @param images optional updated image files
     * @return {@code true} if update was successful
     */
    @PutMapping("update/property")
    public ResponseEntity<Boolean> updatePropertyPost(
            @Valid @RequestPart UpdatePropertyPostDto updatePropertyPostDto,
            @RequestPart(required = false) MultipartFile[] images
    ) {
        Post post = propertyPostService.updatePost(updatePropertyPostDto, images);
        return ResponseEntity.ok(post != null);
    }

    /**
     * Creates a new job post with optional images.
     *
     * @param jobPostDto the job post data
     * @param images optional image files
     * @return {@code true} if creation was successful
     */
    @PostMapping("create/job")
    public ResponseEntity<Boolean> createJobPost(
            @Valid @RequestPart JobPostDto jobPostDto,
            @RequestPart(required = false) MultipartFile[] images
    ) {
        Post post = jobPostService.createPost(jobPostDto, images);
        return ResponseEntity.ok(post != null);
    }

    /**
     * Updates an existing job post with optional images.
     *
     * @param updateJobPostDto the updated job post data
     * @param images optional updated image files
     * @return {@code true} if update was successful
     */
    @PutMapping("update/job")
    public ResponseEntity<Boolean> updateJobPost(
            @Valid @RequestPart UpdateJobPostDto updateJobPostDto,
            @RequestPart(required = false) MultipartFile[] images
    ) {
        Post post = jobPostService.updatePost(updateJobPostDto, images);
        return ResponseEntity.ok(post != null);
    }

    /**
     * Creates a new transaction post with optional images.
     *
     * @param transactionPostDto the transaction post data
     * @param images optional image files
     * @return {@code true} if creation was successful
     */
    @PostMapping("create/transaction")
    public ResponseEntity<Boolean> createTransactionPost(
            @Valid @RequestPart TransactionPostDto transactionPostDto,
            @RequestPart(required = false) MultipartFile[] images
    ) {
        Post post = transactionPostService.createPost(transactionPostDto, images);
        return ResponseEntity.ok(post != null);
    }

    /**
     * Updates an existing transaction post with optional images.
     *
     * @param updateTransactionPostDto the updated transaction post data
     * @param images optional updated image files
     * @return {@code true} if update was successful
     */
    @PutMapping("update/transaction")
    public ResponseEntity<Boolean> updateTransactionPost(
            @Valid @RequestPart UpdateTransactionPostDto updateTransactionPostDto,
            @RequestPart(required = false) MultipartFile[] images
    ) {
        Post post = transactionPostService.updatePost(updateTransactionPostDto, images);
        return ResponseEntity.ok(post != null);
    }

    /**
     * Creates a new society post with optional images.
     *
     * @param societyPostDto the society post data
     * @param images optional image files
     * @return {@code true} if creation was successful
     */
    @PostMapping("create/society")
    public ResponseEntity<Boolean> createSocietyPost(
            @Valid @RequestPart SocietyPostDto societyPostDto,
            @RequestPart(required = false) MultipartFile[] images
    ) {
        Post post = societyPostService.createPost(societyPostDto, images);
        return ResponseEntity.ok(post != null);
    }

    /**
     * Updates an existing society post with optional images.
     *
     * @param updateSocietyPostDto the updated society post data
     * @param images optional updated image files
     * @return {@code true} if update was successful
     */
    @PutMapping("update/society")
    public ResponseEntity<Boolean> updateSocietyPost(
            @Valid @RequestPart UpdateSocietyPostDto updateSocietyPostDto,
            @RequestPart(required = false) MultipartFile[] images
    ) {
        Post post = societyPostService.updatePost(updateSocietyPostDto, images);
        return ResponseEntity.ok(post != null);
    }

    /**
     * Creates a new World Cup post with optional images and cover image.
     *
     * @param worldCupPostDto the World Cup post data
     * @param images optional image files
     * @param coverImage optional cover image
     * @return {@code true} if creation was successful
     */
    @PostMapping("create/worldcup")
    public ResponseEntity<Boolean> createWorldCupPost(
            @Valid @RequestPart WorldCupPostDto worldCupPostDto,
            @RequestPart(required = false) MultipartFile[] images,
            @RequestPart(required = false) MultipartFile coverImage
    ) {
        WorldCupPost post = worldCupPostService.createWorldCupPost(worldCupPostDto, images, coverImage);
        return ResponseEntity.ok(post != null);
    }

    /**
     * Updates an existing World Cup post with optional images and cover image.
     *
     * @param updateWorldCupPostDto the updated World Cup post data
     * @param images optional updated image files
     * @param coverImage optional updated cover image
     * @return {@code true} if update was successful
     */
    @PutMapping("update/worldcup")
    public ResponseEntity<Boolean> updateWorldCupPost(
            @Valid @RequestPart UpdateWorldCupPostDto updateWorldCupPostDto,
            @RequestPart(required = false) MultipartFile[] images,
            @RequestPart(required = false) MultipartFile coverImage
    ) {
        Post post = worldCupPostService.updatePost(updateWorldCupPostDto, images, coverImage);
        return ResponseEntity.ok(post != null);
    }

    /**
     * Creates a new travel post with optional images.
     *
     * @param travelPostDto the travel post data
     * @param images optional image files
     * @return {@code true} if creation was successful
     */
    @PostMapping("create/travel")
    public ResponseEntity<Boolean> createTravelPost(
            @Valid @RequestPart TravelPostDto travelPostDto,
            @RequestPart(required = false) MultipartFile[] images
    ) {
        Post post = travelPostService.createPost(travelPostDto, images);
        return ResponseEntity.ok(post != null);
    }

    /**
     * Updates an existing travel post with optional images.
     *
     * @param updateTravelPostDto the updated travel post data
     * @param images optional updated image files
     * @return {@code true} if update was successful
     */
    @PutMapping("update/travel")
    public ResponseEntity<Boolean> updateTravelPost(
            @Valid @RequestPart UpdateTravelPostDto updateTravelPostDto,
            @RequestPart(required = false) MultipartFile[] images
    ) {
        Post post = travelPostService.updatePost(updateTravelPostDto, images);
        return ResponseEntity.ok(post != null);
    }

    /**
     * Creates a new study post with optional images.
     *
     * @param studyPostDto the study post data
     * @param images optional image files
     * @return {@code true} if creation was successful
     */
    @PostMapping("create/study")
    public ResponseEntity<Boolean> createStudyPost(
            @Valid @RequestPart StudyPostDto studyPostDto,
            @RequestPart(required = false) MultipartFile[] images
    ) {
        Post post = studyPostService.createPost(studyPostDto, images);
        return ResponseEntity.ok(post != null);
    }

    /**
     * Updates an existing study post with optional images.
     *
     * @param updateStudyPostDto the updated study post data
     * @param images optional updated image files
     * @return {@code true} if update was successful
     */
    @PutMapping("update/study")
    public ResponseEntity<Boolean> updateStudyPost(
            @Valid @RequestPart UpdateStudyPostDto updateStudyPostDto,
            @RequestPart(required = false) MultipartFile[] images
    ) {
        Post post = studyPostService.updatePost(updateStudyPostDto, images);
        return ResponseEntity.ok(post != null);
    }

    /**
     * Returns a paginated list of recent article posts.
     *
     * @param page the page number (1-based)
     * @param size the number of items per page
     * @return paginated response containing article posts
     */
    @GetMapping("get/pageable/recent/article")
    public ResponseEntity<ArticlePostPaginationResponse> getRecentPageableArticlePosts(@RequestParam int page, @RequestParam int size) {
        Page<ArticlePost> posts = articlePostService.getCreatedAtDescPostsByPage(PageRequest.of(page - 1, size));
        List<NormalArticlePostDto> postDtoList = posts.getContent()
                .stream()
                .map(post -> post.convertPostToNormalArticlePostDto())
                .collect(Collectors.toList());

        ArticlePostPaginationResponse postPaginationResponse = ArticlePostPaginationResponse.builder().pageSize(posts.getTotalPages()).currentPagePostsCount(posts.getNumberOfElements()).currentPage(page).posts(postDtoList).build();
        return ResponseEntity.ok(postPaginationResponse);
    }

    /**
     * Retrieves a paginated list of recent World Cup posts sorted by the given option.
     *
     * @param page   the page number to retrieve (1-based)
     * @param size   the number of items per page
     * @param option sorting or filtering option (e.g., "latest", "popular")
     * @return {@link WorldCupPostPaginationResponse} containing the current page of World Cup posts
     */
    @GetMapping("get/pageable/recent/worldcup")
    public ResponseEntity<WorldCupPostPaginationResponse> getRecentPageableWorldCupPosts(@RequestParam int page, @RequestParam int size, @RequestParam String option) {
        Page<WorldCupPost> posts = worldCupPostService.getCreatedAtDescPostsByPage(PageRequest.of(page - 1, size), option);
        List<NormalWorldCupPostDto> postDtoList = posts.getContent()
                .stream()
                .map(post -> post.convertPostToNormalCupPostDto())
                .collect(Collectors.toList());

        WorldCupPostPaginationResponse postPaginationResponse = WorldCupPostPaginationResponse.builder().pageSize(posts.getTotalPages()).currentPagePostsCount(posts.getNumberOfElements()).currentPage(page).posts(postDtoList).build();
        return ResponseEntity.ok(postPaginationResponse);
    }

    /**
     * Retrieves a paginated list of recent Property posts with optional filtering by price range and period.
     *
     * @param page      the page number to retrieve (1-based)
     * @param size      the number of items per page
     * @param minPrice  the minimum price to filter by (-1 for no min filter)
     * @param maxPrice  the maximum price to filter by (-1 for no max filter)
     * @param period    the listing period to filter by (e.g., "전체 = All", "1주 = 1 Week", "1달 = 1 Month")
     * @param option    sorting or filtering option (e.g., "latest", "popular")
     * @return {@link PropertyPostPaginationResponse} containing the current page of Property posts
     */
    @GetMapping("get/pageable/recent/property")
    public ResponseEntity<PropertyPostPaginationResponse> getRecentPageablePropertyPosts(@RequestParam int page, @RequestParam int size, @RequestParam Long minPrice, @RequestParam Long maxPrice, @RequestParam String period, @RequestParam String option) {
        Page<PropertyPost> posts;

        Long min = minPrice == -1 ? 0 : minPrice;
        Long max = maxPrice == -1 ? 999999999999999999L : maxPrice;

        if (minPrice == -1 && maxPrice == -1 && period.equals("전체")) {
            posts = propertyPostService.getCreatedAtDescPostsByPage(PageRequest.of(page - 1, size), option);
        }
        else if (minPrice == -1 && maxPrice == -1) {
            posts = propertyPostService.getCreatedAtDescPostsByPageNPeriod(Period.valueOf(period), PageRequest.of(page - 1, size), option);
        }
        else if (period.equals("전체")) {
            posts = propertyPostService.getCreatedAtDescPostsByPageNMinMaxPrice(min, max, PageRequest.of(page - 1, size), option);
        }
        else {
            posts = propertyPostService.getCreatedAtDescPostsByPageNMinMaxPriceNPeriod(min, max, Period.valueOf(period), PageRequest.of(page - 1, size), option);
        }
        List<NormalPropertyPostDto> postDtoList = posts.getContent()
                .stream()
                .map(post -> post.convertPostToNormalPropertyPostDto())
                .collect(Collectors.toList());

        PropertyPostPaginationResponse postPaginationResponse = PropertyPostPaginationResponse.builder().pageSize(posts.getTotalPages()).currentPagePostsCount(posts.getNumberOfElements()).currentPage(page).posts(postDtoList).build();
        return ResponseEntity.ok(postPaginationResponse);
    }

    /**
     * Retrieves a paginated list of recent Job posts with optional filtering by job type.
     *
     * @param page     the page number to retrieve (1-based)
     * @param size     the number of items per page
     * @param jobType  the job type to filter by (e.g., "전체 = All", "정규직 = Full-Time", "계약직 = Contract")
     * @param option   sorting or filtering option
     * @return {@link JobPostPaginationResponse} containing the current page of Job posts
     */
    @GetMapping("get/pageable/recent/job")
    public ResponseEntity<JobPostPaginationResponse> getRecentPageableJobPosts(@RequestParam int page, @RequestParam int size, @RequestParam String jobType, @RequestParam String option) {
        Page<JobPost> posts;

        if (jobType.equals("전체")) {
            posts = jobPostService.getCreatedAtDescPostsByPage(PageRequest.of(page - 1, size), option);
        }
        else {
            posts = jobPostService.getCreatedAtDescPostsByPageNJobType(PageRequest.of(page - 1, size), JobType.valueOf(jobType), option);
        }
        List<NormalJobPostDto> postDtoList = posts.getContent()
                .stream()
                .map(post -> post.convertPostToNormalJobPostDto())
                .collect(Collectors.toList());

        JobPostPaginationResponse postPaginationResponse = JobPostPaginationResponse.builder().pageSize(posts.getTotalPages()).currentPagePostsCount(posts.getNumberOfElements()).currentPage(page).posts(postDtoList).build();
        return ResponseEntity.ok(postPaginationResponse);
    }

    /**
     * Retrieves a paginated list of recent Transaction posts with optional filters for transaction and price types.
     *
     * @param page            the page number to retrieve (1-based)
     * @param size            the number of items per page
     * @param transactionType the transaction type (e.g., "전체 = All", "판매 = Sell", "구매 = Buy")
     * @param priceType       the price type (e.g., "free", "charged")
     * @param option          sorting or filtering option
     * @return {@link TransactionPostPaginationResponse} containing the current page of Transaction posts
     */
    @GetMapping("get/pageable/recent/transaction")
    public ResponseEntity<TransactionPostPaginationResponse> getRecentPageableTransactionPosts(@RequestParam int page, @RequestParam int size, @RequestParam String transactionType, @RequestParam String priceType, @RequestParam String option) {
        Page<TransactionPost> posts;
        if (transactionType.equals("전체") && priceType.equals("전체")) {
            posts = transactionPostService.getCreatedAtDescPostsByPage(PageRequest.of(page - 1, size), option);
        }
        else if (transactionType.equals("전체")) {
            posts = transactionPostService.getCreatedAtDescPostsByPageNPriceType(PageRequest.of(page - 1, size), PriceType.valueOf(priceType), option);
        }
        else if (priceType.equals("전체")) {
            posts = transactionPostService.getCreatedAtDescPostsByPageNTransactionType(PageRequest.of(page - 1, size), TransactionType.valueOf(transactionType), option);
        }
        else {
            posts = transactionPostService.getCreatedAtDescPostsByPageNTransactionTypeNPriceType(PageRequest.of(page - 1, size), TransactionType.valueOf(transactionType), PriceType.valueOf(priceType), option);
        }

        List<NormalTransactionPostDto> postDtoList = posts.getContent()
                .stream()
                .map(post -> post.convertPostToNormalTransactionPostDto())
                .collect(Collectors.toList());

        TransactionPostPaginationResponse postPaginationResponse = TransactionPostPaginationResponse.builder().pageSize(posts.getTotalPages()).currentPagePostsCount(posts.getNumberOfElements()).currentPage(page).posts(postDtoList).build();
        return ResponseEntity.ok(postPaginationResponse);
    }

    /**
     * Retrieves a paginated list of recent Society posts sorted by the given option.
     *
     * @param page   the page number to retrieve (1-based)
     * @param size   the number of items per page
     * @param option sorting or filtering option
     * @return {@link SocietyPostPaginationResponse} containing the current page of Society posts
     */
    @GetMapping("get/pageable/recent/society")
    public ResponseEntity<SocietyPostPaginationResponse> getRecentPageableSocietyPosts(@RequestParam int page, @RequestParam int size, @RequestParam String option) {
        Page<SocietyPost> posts = societyPostService.getCreatedAtDescPostsByPage(PageRequest.of(page - 1, size), option);
        List<NormalSocietyPostDto> postDtoList = posts.getContent()
                .stream()
                .map(post -> post.convertPostToNormalSocietyPostDto())
                .collect(Collectors.toList());

        SocietyPostPaginationResponse postPaginationResponse = SocietyPostPaginationResponse.builder().pageSize(posts.getTotalPages()).currentPagePostsCount(posts.getNumberOfElements()).currentPage(page).posts(postDtoList).build();
        return ResponseEntity.ok(postPaginationResponse);
    }

    /**
     * Retrieves a paginated list of recent Travel posts, optionally filtered by suburb.
     *
     * @param page          the page number to retrieve (1-based)
     * @param size          the number of items per page
     * @param travelSuburb  the suburb filter ("전체 = All" for no filter)
     * @param option        sorting or filtering option (e.g., "latest", "popular")
     * @return {@link TravelPostPaginationResponse} containing the current page of Travel posts
     */
    @GetMapping("get/pageable/recent/travel")
    public ResponseEntity<TravelPostPaginationResponse> getRecentPageableTravelPosts(@RequestParam int page, @RequestParam int size, @RequestParam String travelSuburb, @RequestParam String option) {
        Page<TravelPost> posts;
        if (travelSuburb.equals("전체")) {
            posts = travelPostService.getCreatedAtDescPostsByPage(PageRequest.of(page - 1, size), option);
        }
        else {
            posts = travelPostService.getCreatedAtDescPostsByPageNSuburb(PageRequest.of(page - 1, size), travelSuburb, option);
        }

        List<NormalTravelPostDto> postDtoList = posts.getContent()
                .stream()
                .map(post -> post.convertPostToNormalTravelPostDto())
                .collect(Collectors.toList());

        TravelPostPaginationResponse postPaginationResponse = TravelPostPaginationResponse.builder().pageSize(posts.getTotalPages()).currentPagePostsCount(posts.getNumberOfElements()).currentPage(page).posts(postDtoList).build();
        return ResponseEntity.ok(postPaginationResponse);
    }

    /**
     * Retrieves a paginated list of recent Study posts sorted by the given option.
     *
     * @param page    the page number to retrieve (1-based)
     * @param size    the number of items per page
     * @param option  sorting or filtering option (e.g., "latest", "popular")
     * @return {@link StudyPostPaginationResponse} containing the current page of Study posts
     */
    @GetMapping("get/pageable/recent/study")
    public ResponseEntity<StudyPostPaginationResponse> getRecentPageableStudyPosts(@RequestParam int page, @RequestParam int size, @RequestParam String option) {
        Page<StudyPost> posts = studyPostService.getCreatedAtDescPostsByPage(PageRequest.of(page - 1, size), option);
        List<NormalStudyPostDto> postDtoList = posts.getContent()
                .stream()
                .map(post -> post.convertPostToNormalStudyPostDto())
                .collect(Collectors.toList());

        StudyPostPaginationResponse postPaginationResponse = StudyPostPaginationResponse.builder().pageSize(posts.getTotalPages()).currentPagePostsCount(posts.getNumberOfElements()).currentPage(page).posts(postDtoList).build();
        return ResponseEntity.ok(postPaginationResponse);
    }

    /**
     * Retrieves a paginated list of recent World Cup posts filtered by subcategory.
     *
     * @param page         the page number to retrieve (1-based)
     * @param size         the number of items per page
     * @param subCategory  the World Cup post subcategory filter
     * @param option       sorting or filtering option
     * @return {@link WorldCupPostPaginationResponse} containing the current page of filtered World Cup posts
     */
    @GetMapping("get/pageable/worldcup/subcategory")
    public ResponseEntity<WorldCupPostPaginationResponse> getRecentPageableWorldCupPosts(@RequestParam int page, @RequestParam int size, @RequestParam SubCategory subCategory, @RequestParam String option) {
        Page<WorldCupPost> posts = worldCupPostService.getCreatedAtDescPostsByPageNSubCategory(subCategory, PageRequest.of(page - 1, size), option);
        List<NormalWorldCupPostDto> postDtoList = posts.getContent()
                .stream()
                .map(post -> post.convertPostToNormalCupPostDto())
                .collect(Collectors.toList());

        WorldCupPostPaginationResponse postPaginationResponse = WorldCupPostPaginationResponse.builder().pageSize(posts.getTotalPages()).currentPagePostsCount(posts.getNumberOfElements()).currentPage(page).posts(postDtoList).build();
        return ResponseEntity.ok(postPaginationResponse);
    }

    /**
     * Retrieves a paginated list of recent Property posts filtered by subcategory, price range, and period.
     *
     * @param page         the page number to retrieve (1-based)
     * @param size         the number of items per page
     * @param subCategory  the property post subcategory filter
     * @param minPrice     minimum price filter (-1 for no minimum)
     * @param maxPrice     maximum price filter (-1 for no maximum)
     * @param period       the period filter (e.g., "전체 = All", "1주 = 1 Week", "1달 = 1 Month")
     * @param option       sorting or filtering option
     * @return {@link PropertyPostPaginationResponse} containing the current page of filtered Property posts
     */
    @GetMapping("get/pageable/property/subcategory")
    public ResponseEntity<PropertyPostPaginationResponse> getRecentPageablePropertyPosts(@RequestParam int page, @RequestParam int size, @RequestParam SubCategory subCategory, @RequestParam Long minPrice, @RequestParam Long maxPrice, @RequestParam String period, @RequestParam String option) {
        Page<PropertyPost> posts;

        Long min = minPrice == -1 ? 0 : minPrice;
        Long max = maxPrice == -1 ? 999999999999999999L : maxPrice;
        if (minPrice == -1 && maxPrice == -1 && period.equals("전체")) {
            posts = propertyPostService.getCreatedAtDescPostsByPageNSubCategory(subCategory, PageRequest.of(page - 1, size), option);
        }
        else if (minPrice == -1 && maxPrice == -1) {
            posts = propertyPostService.getCreatedAtDescPostsByPageNSubCategoryNPeriod(Period.valueOf(period), subCategory, PageRequest.of(page - 1, size), option);
        }
        else if (period.equals("전체")) {
            posts = propertyPostService.getCreatedAtDescPostsByPageNSubCategoryNMinMaxPrice(min, max, subCategory, PageRequest.of(page - 1, size), option);
        }
        else {
            posts = propertyPostService.getCreatedAtDescPostsByPageNSubCategoryNMinMaxPriceNPeriod(min, max, Period.valueOf(period), subCategory, PageRequest.of(page - 1, size), option);
        }

        List<NormalPropertyPostDto> postDtoList = posts.getContent()
                .stream()
                .map(post -> post.convertPostToNormalPropertyPostDto())
                .collect(Collectors.toList());

        PropertyPostPaginationResponse postPaginationResponse = PropertyPostPaginationResponse.builder().pageSize(posts.getTotalPages()).currentPagePostsCount(posts.getNumberOfElements()).currentPage(page).posts(postDtoList).build();
        return ResponseEntity.ok(postPaginationResponse);
    }

    /**
     * Retrieves a paginated list of recent Job posts filtered by subcategory and job type.
     *
     * @param page        the page number to retrieve (1-based)
     * @param size        the number of items per page
     * @param subCategory the job post subcategory filter
     * @param jobType     the job type filter (e.g., "전체 = All", "정규직 = Full-Time", "파트타임 = Part-Time")
     * @param option      sorting or filtering option
     * @return {@link JobPostPaginationResponse} containing the current page of filtered Job posts
     */
    @GetMapping("get/pageable/job/subcategory")
    public ResponseEntity<JobPostPaginationResponse> getRecentPageableJobPosts(@RequestParam int page, @RequestParam int size, @RequestParam SubCategory subCategory, @RequestParam String jobType, @RequestParam String option) {
        Page<JobPost> posts;
        if (jobType.equals("전체")) {
            posts = jobPostService.getCreatedAtDescPostsByPageNSubCategory(subCategory, PageRequest.of(page - 1, size), option);
        }
        else {
            posts = jobPostService.getCreatedAtDescPostsByPageNSubCategoryNJobType(subCategory, JobType.valueOf(jobType), PageRequest.of(page - 1, size), option);
        }

        List<NormalJobPostDto> postDtoList = posts.getContent()
                .stream()
                .map(post -> post.convertPostToNormalJobPostDto())
                .collect(Collectors.toList());

        JobPostPaginationResponse postPaginationResponse = JobPostPaginationResponse.builder().pageSize(posts.getTotalPages()).currentPagePostsCount(posts.getNumberOfElements()).currentPage(page).posts(postDtoList).build();
        return ResponseEntity.ok(postPaginationResponse);
    }

    /**
     * Retrieves a paginated list of recent Transaction posts filtered by subcategory, transaction type, and price type.
     *
     * @param page            the page number to retrieve (1-based)
     * @param size            the number of items per page
     * @param subCategory     the transaction post subcategory filter
     * @param transactionType the transaction type filter (e.g., "전체 = All", "구매 = Buy", "판매 = Sell")
     * @param priceType       the price type filter (e.g., "전체 = All", "무료 = Free", "유료 = Charged")
     * @param option          sorting or filtering option
     * @return {@link TransactionPostPaginationResponse} containing the current page of filtered Transaction posts
     */
    @GetMapping("get/pageable/transaction/subcategory")
    public ResponseEntity<TransactionPostPaginationResponse> getRecentPageableTransactionPosts(@RequestParam int page, @RequestParam int size, @RequestParam SubCategory subCategory, @RequestParam String transactionType, @RequestParam String priceType, @RequestParam String option) {
        Page<TransactionPost> posts;
        if (transactionType.equals("전체") && priceType.equals("전체")) {
            posts = transactionPostService.getCreatedAtDescPostsByPageNSubCategory(subCategory, PageRequest.of(page - 1, size), option);
        }
        else if (transactionType.equals("전체")) {
            posts = transactionPostService.getCreatedAtDescPostsByPageNSubCategoryNPriceType(subCategory, PriceType.valueOf(priceType), PageRequest.of(page - 1, size), option);
        }
        else if (priceType.equals("전체")) {
            posts = transactionPostService.getCreatedAtDescPostsByPageNSubCategoryNTransactionType(subCategory, TransactionType.valueOf(transactionType), PageRequest.of(page - 1, size), option);
        }
        else {
            posts = transactionPostService.getCreatedAtDescPostsByPageNSubCategoryNTransactionTypeNPriceType(subCategory, TransactionType.valueOf(transactionType), PriceType.valueOf(priceType), PageRequest.of(page - 1, size), option);
        }
        List<NormalTransactionPostDto> postDtoList = posts.getContent()
                .stream()
                .map(post -> post.convertPostToNormalTransactionPostDto())
                .collect(Collectors.toList());

        TransactionPostPaginationResponse postPaginationResponse = TransactionPostPaginationResponse.builder().pageSize(posts.getTotalPages()).currentPagePostsCount(posts.getNumberOfElements()).currentPage(page).posts(postDtoList).build();
        return ResponseEntity.ok(postPaginationResponse);
    }

    /**
     * Retrieves a paginated list of recent Society posts filtered by subcategory.
     *
     * @param page        the page number to retrieve (1-based)
     * @param size        the number of items per page
     * @param subCategory the society post subcategory filter
     * @param option      sorting or filtering option
     * @return {@link SocietyPostPaginationResponse} containing the current page of filtered Society posts
     */
    @GetMapping("get/pageable/society/subcategory")
    public ResponseEntity<SocietyPostPaginationResponse> getRecentPageableSocietyPosts(@RequestParam int page, @RequestParam int size, @RequestParam SubCategory subCategory, @RequestParam String option) {
        Page<SocietyPost> posts = societyPostService.getCreatedAtDescPostsByPageNSubCategory(subCategory, PageRequest.of(page - 1, size), option);
        List<NormalSocietyPostDto> postDtoList = posts.getContent()
                .stream()
                .map(post -> post.convertPostToNormalSocietyPostDto())
                .collect(Collectors.toList());

        SocietyPostPaginationResponse postPaginationResponse = SocietyPostPaginationResponse.builder().pageSize(posts.getTotalPages()).currentPagePostsCount(posts.getNumberOfElements()).currentPage(page).posts(postDtoList).build();
        return ResponseEntity.ok(postPaginationResponse);
    }

    /**
     * Retrieves a paginated list of recent Travel posts filtered by subcategory and suburb.
     *
     * @param page         the page number to retrieve (1-based)
     * @param size         the number of items per page
     * @param subCategory  the travel post subcategory filter
     * @param travelSuburb the suburb filter ("전체 = All" for no suburb filter)
     * @param option       sorting or filtering option
     * @return {@link TravelPostPaginationResponse} containing the current page of filtered Travel posts
     */
    @GetMapping("get/pageable/travel/subcategory")
    public ResponseEntity<TravelPostPaginationResponse> getRecentPageableTravelPosts(@RequestParam int page, @RequestParam int size, @RequestParam SubCategory subCategory, @RequestParam String travelSuburb, @RequestParam String option) {
        Page<TravelPost> posts;
        if (travelSuburb.equals("전체")) {
            posts = travelPostService.getCreatedAtDescPostsByPageNSubCategory(subCategory, PageRequest.of(page - 1, size), option);
        }
        else {
            posts = travelPostService.getCreatedAtDescPostsByPageNSubCategoryNSuburb(subCategory, PageRequest.of(page - 1, size), travelSuburb, option);
        }

        List<NormalTravelPostDto> postDtoList = posts.getContent()
                .stream()
                .map(post -> post.convertPostToNormalTravelPostDto())
                .collect(Collectors.toList());

        TravelPostPaginationResponse postPaginationResponse = TravelPostPaginationResponse.builder().pageSize(posts.getTotalPages()).currentPagePostsCount(posts.getNumberOfElements()).currentPage(page).posts(postDtoList).build();
        return ResponseEntity.ok(postPaginationResponse);
    }

    /**
     * Retrieves a paginated list of recent Study posts filtered by subcategory.
     *
     * @param page        the page number to retrieve (1-based)
     * @param size        the number of items per page
     * @param subCategory the study post subcategory filter
     * @param option      sorting or filtering option
     * @return {@link StudyPostPaginationResponse} containing the current page of filtered Study posts
     */
    @GetMapping("get/pageable/study/subcategory")
    public ResponseEntity<StudyPostPaginationResponse> getRecentPageableStudyPosts(@RequestParam int page, @RequestParam int size, @RequestParam SubCategory subCategory, @RequestParam String option) {
        Page<StudyPost> posts = studyPostService.getCreatedAtDescPostsByPageNSubCategory(subCategory, PageRequest.of(page - 1, size), option);
        List<NormalStudyPostDto> postDtoList = posts.getContent()
                .stream()
                .map(post -> post.convertPostToNormalStudyPostDto())
                .collect(Collectors.toList());

        StudyPostPaginationResponse postPaginationResponse = StudyPostPaginationResponse.builder().pageSize(posts.getTotalPages()).currentPagePostsCount(posts.getNumberOfElements()).currentPage(page).posts(postDtoList).build();
        return ResponseEntity.ok(postPaginationResponse);
    }

    /**
     * Retrieves a paginated list of the current user's own Article posts.
     *
     * @param userId the ID of the user (extracted from request header)
     * @param page   the page number to retrieve (1-based)
     * @param size   the number of items per page
     * @return {@link ArticlePostPaginationResponse} containing the current page of user's own Article posts
     */
    @GetMapping("get/pageable/own/articles")
    public ResponseEntity<ArticlePostPaginationResponse> getPageableOwnArticles(@RequestHeader int userId, @RequestParam int page, @RequestParam int size) {
        Page<ArticlePost> posts = articlePostService.getAllPostsByPageNUser(Long.valueOf(userId), PageRequest.of(page - 1, size));
        List<NormalArticlePostDto> normalArticlePostDtoList = posts.getContent()
                .stream()
                .map(post -> post.convertPostToNormalArticlePostDto())
                .collect(Collectors.toList());

        ArticlePostPaginationResponse postPaginationResponse = ArticlePostPaginationResponse.builder().pageSize(posts.getTotalPages()).currentPagePostsCount(posts.getNumberOfElements()).currentPage(page).posts(normalArticlePostDtoList).build();
        return ResponseEntity.ok(postPaginationResponse);
    }

    /**
     * Retrieves a paginated list of Article posts written by others (excluding the current user).
     *
     * @param userId the ID of the current user (extracted from request header)
     * @param page   the page number to retrieve (1-based)
     * @param size   the number of items per page
     * @return {@link ArticlePostPaginationResponse} containing the current page of Article posts by others
     */
    @GetMapping("get/pageable/others/articles")
    public ResponseEntity<ArticlePostPaginationResponse> getPageableOthersArticles(@RequestHeader int userId, @RequestParam int page, @RequestParam int size) {
        Page<ArticlePost> posts = articlePostService.getAllPostsByPageNUser(Long.valueOf(userId), PageRequest.of(page - 1, size));
        List<NormalArticlePostDto> normalArticlePostDtoList = posts.getContent()
                .stream()
                .map(post -> post.convertPostToNormalArticlePostDto())
                .collect(Collectors.toList());

        ArticlePostPaginationResponse postPaginationResponse = ArticlePostPaginationResponse.builder().pageSize(posts.getTotalPages()).currentPagePostsCount(posts.getNumberOfElements()).currentPage(page).posts(normalArticlePostDtoList).build();
        return ResponseEntity.ok(postPaginationResponse);
    }

    /**
     * Retrieves a paginated list of the currently authenticated user's own posts.
     * Pinnable posts, if present, are prioritized to appear at the top of the result list.
     *
     * @param userId the ID of the current user making the request (extracted from request header)
     * @param page   the page number to retrieve (1-based index)
     * @param size   the number of posts to include per page
     * @return {@link WholePostPaginationResponse} containing metadata and the list of summarized posts for the requested page
     */
    @GetMapping("get/pageable/own/posts")
    public ResponseEntity<WholePostPaginationResponse> getPageableOwnPosts(@RequestHeader int userId, @RequestParam int page, @RequestParam int size) {
        Page<PinnablePost> posts = pinnablePostService.getAllPostsByPageNUser(Long.valueOf(userId), PageRequest.of(page - 1, size));
        List<SummarizedPostDto> summarizedPostDtoList = posts.getContent()
                .stream()
                .map(post -> post.convertToSummarizedPostDto())
                .collect(Collectors.toList());

        WholePostPaginationResponse postPaginationResponse = WholePostPaginationResponse.builder().pageSize(posts.getTotalPages()).currentPagePostsCount(posts.getNumberOfElements()).currentPage(page).posts(summarizedPostDtoList).build();
        return ResponseEntity.ok(postPaginationResponse);
    }

    /**
     * Retrieves a paginated list of posts written by another user (excluding the current user),
     * with potential prioritization of pinnable posts.
     *
     * @param userId the ID of the user whose posts are to be retrieved (extracted from request header)
     * @param page   the page number to retrieve (1-based)
     * @param size   the number of items per page
     * @return {@link WholePostPaginationResponse} containing the current page of another user's summarized posts
     */
    @GetMapping("get/pageable/others/posts")
    public ResponseEntity<WholePostPaginationResponse> getPageableOthersPosts(@RequestHeader int userId, @RequestParam int page, @RequestParam int size) {
        Page<PinnablePost> posts = pinnablePostService.getAllPostsByPageNUser(Long.valueOf(userId), PageRequest.of(page - 1, size));
        List<SummarizedPostDto> summarizedPostDtoList = posts.getContent()
                .stream()
                .map(post -> post.convertToSummarizedPostDto())
                .collect(Collectors.toList());

        WholePostPaginationResponse postPaginationResponse = WholePostPaginationResponse.builder().pageSize(posts.getTotalPages()).currentPagePostsCount(posts.getNumberOfElements()).currentPage(page).posts(summarizedPostDtoList).build();
        return ResponseEntity.ok(postPaginationResponse);
    }

    /**
     * Retrieves a paginated list of posts liked by the specified user.
     *
     * @param userId the ID of the user who liked the posts (extracted from request header)
     * @param page   the page number to retrieve (1-based index)
     * @param size   the number of posts to include per page
     * @return {@link WholePostPaginationResponse} containing metadata and a list of summarized liked posts for the requested page
     */
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

    /**
     * Retrieves the 5 most recently created World Cup posts.
     *
     * @return a list of {@link SummarizedWorldCupPostDto} representing the latest World Cup posts
     */
    @GetMapping("get/recent-5/worldcup/post")
    public ResponseEntity<List<SummarizedWorldCupPostDto>> getRecent5WorldCupPost() {
        List<WorldCupPost> worldCupPostList = worldCupPostService.getRecent5Posts();
        List<SummarizedWorldCupPostDto> summarizedWorldCupPostDtoList = worldCupPostList
                .stream()
                .map(post -> post.convertPostToSummarizedWorldCupPostDto())
                .collect(Collectors.toList());

        return ResponseEntity.ok(summarizedWorldCupPostDtoList);
    }

    /**
     * Retrieves the 5 most recently created Property posts.
     *
     * @return a list of {@link SummarizedPropertyPostDto} representing the latest Property posts
     */
    @GetMapping("get/recent-5/property/post")
    public ResponseEntity<List<SummarizedPropertyPostDto>> getRecent5PropertyPost() {
        List<PropertyPost> propertyPostList = propertyPostService.getRecent5Posts();
        List<SummarizedPropertyPostDto> summarizedPropertyPostDtoList = propertyPostList
                .stream()
                .map(post -> post.convertPostToSummarizedPropertyPostDto())
                .collect(Collectors.toList());

        return ResponseEntity.ok(summarizedPropertyPostDtoList);
    }

    /**
     * Retrieves the 5 most recently created Job posts.
     *
     * @return a list of {@link SummarizedJobPostDto} representing the latest Job posts
     */
    @GetMapping("get/recent-5/job/post")
    public ResponseEntity<List<SummarizedJobPostDto>> getRecent5JobPost() {
        List<JobPost> jobPostList = jobPostService.getRecent5Posts();
        List<SummarizedJobPostDto> summarizedJobPostDtoList = jobPostList
                .stream()
                .map(post -> post.convertPostToSummarizedJobPostDto())
                .collect(Collectors.toList());

        return ResponseEntity.ok(summarizedJobPostDtoList);
    }

    /**
     * Retrieves the 5 most recently created Transaction posts.
     *
     * @return a list of {@link SummarizedTransactionPostDto} representing the latest Transaction posts
     */
    @GetMapping("get/recent-5/transaction/post")
    public ResponseEntity<List<SummarizedTransactionPostDto>> getRecent5TransactionPost() {
        List<TransactionPost> transactionPostList = transactionPostService.getRecent5Posts();
        List<SummarizedTransactionPostDto> summarizedTransactionPostDtoList = transactionPostList
                .stream()
                .map(post -> post.convertPostToSummarizedTransactionPostDto())
                .collect(Collectors.toList());

        return ResponseEntity.ok(summarizedTransactionPostDtoList);
    }

    /**
     * Retrieves the 5 most recently created Society posts.
     *
     * @return a list of {@link SummarizedSocietyPostDto} representing the latest Society posts
     */
    @GetMapping("get/recent-5/society/post")
    public ResponseEntity<List<SummarizedSocietyPostDto>> getRecent5SocietyPost() {
        List<SocietyPost> societyPostList = societyPostService.getRecent5Posts();
        List<SummarizedSocietyPostDto> summarizedSocietyPostDtoList = societyPostList
                .stream()
                .map(post -> post.convertPostToSummarizedSocietyPostDto())
                .collect(Collectors.toList());

        return ResponseEntity.ok(summarizedSocietyPostDtoList);
    }

    /**
     * Retrieves the 5 most recently created Travel posts.
     *
     * @return a list of {@link SummarizedTravelPostDto} representing the latest Travel posts
     */
    @GetMapping("get/recent-5/travel/post")
    public ResponseEntity<List<SummarizedTravelPostDto>> getRecent5TravelPost() {
        List<TravelPost> travelPostList = travelPostService.getRecent5Posts();
        List<SummarizedTravelPostDto> summarizedTravelPostDtoList = travelPostList
                .stream()
                .map(post -> post.convertPostToSummarizedTravelPostDto())
                .collect(Collectors.toList());

        return ResponseEntity.ok(summarizedTravelPostDtoList);
    }

    /**
     * Retrieves the 5 most recently created Study posts.
     *
     * @return a list of {@link SummarizedStudyPostDto} representing the latest Study posts
     */
    @GetMapping("get/recent-5/study/post")
    public ResponseEntity<List<SummarizedStudyPostDto>> getRecent5StudyPost() {
        List<StudyPost> studyPostList = studyPostService.getRecent5Posts();
        List<SummarizedStudyPostDto> summarizedStudyPostDtoList = studyPostList
                .stream()
                .map(post -> post.convertPostToSummarizedStudyPostDto())
                .collect(Collectors.toList());

        return ResponseEntity.ok(summarizedStudyPostDtoList);
    }

    /**
     * Retrieves the list of {@link CandidateDto} for a specific World Cup post.
     *
     * @param postId the ID of the World Cup post
     * @return a list of {@link CandidateDto} associated with the given post
     */
    @GetMapping("get/specific/candidateDtoList")
    public ResponseEntity<List<CandidateDto>> getSpecificCandidateDtoList(@RequestParam Long postId) {
        WorldCupPost worldCupPost = worldCupPostService.getPostById(postId);
        List<Candidate> candidates = worldCupPost.getCandidates();
        List<CandidateDto> candidateDtoList = candidates.stream().map(candidate -> candidate.convertToCandidateDto()).collect(Collectors.toList());
        return ResponseEntity.ok(candidateDtoList);
    }

    /**
     * Retrieves detailed information about a specific Article post and increments its view count.
     *
     * @param postId the ID of the Article post to retrieve
     * @param userId the ID of the requesting user (from request header)
     * @return {@link DetailedArticlePostDto} containing the full details of the article
     */
    @GetMapping("get/specific/article")
    public ResponseEntity<DetailedArticlePostDto> getSpecificArticlePost(@RequestParam Long postId, @RequestHeader String userId) {
        ArticlePost articlePost = articlePostService.getPostById(postId);
        DetailedArticlePostDto detailedArticlePostDto = articlePost.convertPostToDetailedArticlePostDto(userId);
        postService.addViewCount(postId);
        return ResponseEntity.ok(detailedArticlePostDto);
    }

    /**
     * Retrieves detailed information about a specific World Cup post and increments its view count.
     *
     * @param postId the ID of the World Cup post
     * @param userId the ID of the requesting user (from request header)
     * @return {@link DetailedWorldCupPostDto} containing the full details of the World Cup post
     */
    @GetMapping("get/specific/worldcup")
    public ResponseEntity<DetailedWorldCupPostDto> getSpecificWorldCupPost(@RequestParam Long postId, @RequestHeader String userId) {
        WorldCupPost worldCupPost = worldCupPostService.getPostById(postId);
        DetailedWorldCupPostDto detailedWorldCupPostDto = worldCupPost.convertPostToDetailedWorldCupPostDto(userId);
        postService.addViewCount(postId);
        return ResponseEntity.ok(detailedWorldCupPostDto);
    }

    /**
     * Retrieves editable details of a specific World Cup post for update purposes.
     *
     * @param postId the ID of the World Cup post
     * @return {@link UpdateDetailedWorldCupPostDto} containing the editable fields of the World Cup post
     */
    @GetMapping("get/specific/update/worldcup")
    public ResponseEntity<UpdateDetailedWorldCupPostDto> getSpecificWorldCupPost(@RequestParam Long postId) {
        WorldCupPost worldCupPost = worldCupPostService.getPostById(postId);
        UpdateDetailedWorldCupPostDto updateDetailedWorldCupPostDto = worldCupPost.convertPostToUpdatePostDto();
        return ResponseEntity.ok(updateDetailedWorldCupPostDto);
    }

    /**
     * Retrieves detailed information about a specific Property post and increments its view count.
     *
     * @param postId the ID of the Property post
     * @param userId the ID of the requesting user (from request header)
     * @return {@link DetailedPropertyPostDto} containing the full details of the Property post
     */
    @GetMapping("get/specific/property")
    public ResponseEntity<DetailedPropertyPostDto> getSpecificPropertyPost(@RequestParam Long postId, @RequestHeader String userId) {
        PropertyPost propertyPost = propertyPostService.getPostById(postId);
        DetailedPropertyPostDto detailedPropertyPostDto = propertyPost.convertPostToDetailedPropertyPostDto(userId);
        postService.addViewCount(postId);

        return ResponseEntity.ok(detailedPropertyPostDto);
    }

    /**
     * Retrieves detailed information about a specific Job post and increments its view count.
     *
     * @param postId the ID of the Job post
     * @param userId the ID of the requesting user (from request header)
     * @return {@link DetailedJobPostDto} containing the full details of the Job post
     */
    @GetMapping("get/specific/job")
    public ResponseEntity<DetailedJobPostDto> getSpecificJobPost(@RequestParam Long postId, @RequestHeader String userId) {
        JobPost jobPost = jobPostService.getPostById(postId);
        DetailedJobPostDto detailedJobPostDto = jobPost.convertPostToDetailedJobPostDto(userId);
        postService.addViewCount(postId);

        return ResponseEntity.ok(detailedJobPostDto);
    }

    /**
     * Retrieves detailed information about a specific Transaction post and increments its view count.
     *
     * @param postId the ID of the Transaction post
     * @param userId the ID of the requesting user (from request header)
     * @return {@link DetailedTransactionPostDto} containing the full details of the Transaction post
     */
    @GetMapping("get/specific/transaction")
    public ResponseEntity<DetailedTransactionPostDto> getSpecificTransactionPost(@RequestParam Long postId, @RequestHeader String userId) {
        TransactionPost transactionPost = transactionPostService.getPostById(postId);
        DetailedTransactionPostDto detailedTransactionPostDto = transactionPost.convertPostToDetailedTransactionPostDto(userId);
        postService.addViewCount(postId);
        return ResponseEntity.ok(detailedTransactionPostDto);
    }

    /**
     * Retrieves detailed information about a specific Society post and increments its view count.
     *
     * @param postId the ID of the Society post
     * @param userId the ID of the requesting user (from request header)
     * @return {@link DetailedSocietyPostDto} containing the full details of the Society post
     */
    @GetMapping("get/specific/society")
    public ResponseEntity<DetailedSocietyPostDto> getSpecificSocietyPost(@RequestParam Long postId, @RequestHeader String userId) {
        SocietyPost societyPost = societyPostService.getPostById(postId);
        DetailedSocietyPostDto detailedSocietyPostDto = societyPost.convertPostToDetailedSocietyPostDto(userId);
        postService.addViewCount(postId);
        return ResponseEntity.ok(detailedSocietyPostDto);
    }

    /**
     * Retrieves detailed information about a specific Travel post and increments its view count.
     *
     * @param postId the ID of the Travel post
     * @param userId the ID of the requesting user (from request header)
     * @return {@link DetailedTravelPostDto} containing the full details of the Travel post
     */
    @GetMapping("get/specific/travel")
    public ResponseEntity<DetailedTravelPostDto> getSpecificTravelPost(@RequestParam Long postId, @RequestHeader String userId) {
        TravelPost travelPost = travelPostService.getPostById(postId);
        DetailedTravelPostDto detailedTravelPostDto = travelPost.convertPostToDetailedTravelPostDto(userId);
        postService.addViewCount(postId);

        return ResponseEntity.ok(detailedTravelPostDto);
    }

    /**
     * Retrieves editable details of a specific Article post for update purposes.
     *
     * @param postId the ID of the Article post
     * @return {@link UpdateArticlePostDto} containing the editable fields of the Article post
     */
    @GetMapping("get/update/articleDto")
    public ResponseEntity<UpdateArticlePostDto> getUpdateArticlePostDto(@RequestParam Long postId) {
        ArticlePost articlePost = articlePostService.getPostById(postId);
        UpdateArticlePostDto updateArticlePostDto = articlePost.convertPostToUpdateArticlePostDto();
        return ResponseEntity.ok(updateArticlePostDto);
    }

    /**
     * Retrieves editable details of a specific Property post for update purposes.
     *
     * @param postId the ID of the Property post
     * @return {@link UpdatePropertyPostDto} containing the editable fields of the Property post
     */
    @GetMapping("get/update/propertyDto")
    public ResponseEntity<UpdatePropertyPostDto> getUpdatePropertyPostDto(@RequestParam Long postId) {
        PropertyPost propertyPost = propertyPostService.getPostById(postId);
        UpdatePropertyPostDto updateTravelPostDto = propertyPost.convertToUpdatePropertyPostDto();
        return ResponseEntity.ok(updateTravelPostDto);
    }

    /**
     * Retrieves editable details of a specific Job post for update purposes.
     *
     * @param postId the ID of the Job post
     * @return {@link UpdateJobPostDto} containing the editable fields of the Job post
     */
    @GetMapping("get/update/jobDto")
    public ResponseEntity<UpdateJobPostDto> getUpdateJobPostDto(@RequestParam Long postId) {
        JobPost jobPost = jobPostService.getPostById(postId);
        UpdateJobPostDto updateJobPostDto = jobPost.convertToUpdateJobPostDto();
        return ResponseEntity.ok(updateJobPostDto);
    }

    /**
     * Retrieves editable details of a specific Transaction post for update purposes.
     *
     * @param postId the ID of the Transaction post
     * @return {@link UpdateTransactionPostDto} containing the editable fields of the Transaction post
     */
    @GetMapping("get/update/transactionDto")
    public ResponseEntity<UpdateTransactionPostDto> getUpdateTransactionPostDto(@RequestParam Long postId) {
        TransactionPost transactionPost = transactionPostService.getPostById(postId);
        UpdateTransactionPostDto updateTransactionPostDto = transactionPost.convertToUpdateTransactionPostDto();
        return ResponseEntity.ok(updateTransactionPostDto);
    }

    /**
     * Retrieves editable details of a specific Travel post for update purposes.
     *
     * @param postId the ID of the Travel post
     * @return {@link UpdateTravelPostDto} containing the editable fields of the Travel post
     */
    @GetMapping("get/update/travelDto")
    public ResponseEntity<UpdateTravelPostDto> getUpdateTravelPostDto(@RequestParam Long postId) {
        TravelPost travelPost = travelPostService.getPostById(postId);
        UpdateTravelPostDto travelPostDto = travelPost.convertToUpdateTravelPostDto();
        return ResponseEntity.ok(travelPostDto);
    }

    /**
     * Retrieves editable details of a specific Study post for update purposes.
     *
     * @param postId the ID of the Study post
     * @return {@link UpdateStudyPostDto} containing the editable fields of the Study post
     */
    @GetMapping("get/update/studyDto")
    public ResponseEntity<UpdateStudyPostDto> getUpdateStudyPostDto(@RequestParam Long postId) {
        StudyPost studyPost = studyPostService.getPostById(postId);
        UpdateStudyPostDto studyPostDto = studyPost.convertToUpdateStudyPostDto();
        return ResponseEntity.ok(studyPostDto);
    }

    /**
     * Retrieves editable details of a specific Society post for update purposes.
     *
     * @param postId the ID of the Society post
     * @return {@link UpdateSocietyPostDto} containing the editable fields of the Society post
     */
    @GetMapping("get/update/societyDto")
    public ResponseEntity<UpdateSocietyPostDto> getUpdateSocietyPostDto(@RequestParam Long postId) {
        SocietyPost societyPost = societyPostService.getPostById(postId);
        UpdateSocietyPostDto societyPostDto = societyPost.convertToUpdateSocietyPostDto();
        return ResponseEntity.ok(societyPostDto);
    }

    /**
     * Retrieves detailed information about a specific Study post and increments its view count.
     *
     * @param postId the ID of the Study post
     * @param userId the ID of the requesting user (from request header)
     * @return {@link DetailedStudyPostDto} containing the full details of the Study post
     */
    @GetMapping("get/specific/study")
    public ResponseEntity<DetailedStudyPostDto> getSpecificStudyPost(@RequestParam Long postId, @RequestHeader String userId) {
        StudyPost studyPost = studyPostService.getPostById(postId);
        DetailedStudyPostDto detailedStudyPostDto = studyPost.convertPostToDetailedStudyPostDto(userId);
        postService.addViewCount(postId);

        return ResponseEntity.ok(detailedStudyPostDto);
    }

    /**
     * Retrieves a summarized version of a specific post owned by the user.
     *
     * @param postId the ID of the post
     * @return {@link SummarizedPostDto} containing the summarized post information
     */
    @GetMapping("get/summarised/specific/post")
    public ResponseEntity<SummarizedPostDto> getSummarizedOwnPost(@RequestParam Long postId) {
        PinnablePost post = pinnablePostService.getPostById(postId);
        SummarizedPostDto summarizedPostDto = post.convertToSummarizedPostDto();
        return ResponseEntity.ok(summarizedPostDto);
    }

    /**
     * Searches for World Cup posts based on title, sub-category, suburb, and optional keywords.
     *
     * @param title       the post title to search
     * @param subCategory the sub-category of the post
     * @param suburb      the suburb related to the post
     * @param keywords    optional list of keywords to filter the results
     * @param page        the page number to retrieve (1-based)
     * @param size        the number of items per page
     * @return {@link WorldCupPostPaginationResponse} containing paginated search results
     */
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

    /**
     * Searches for Property posts based on title, sub-category, suburb, and optional keywords.
     *
     * @param title       the post title to search
     * @param subCategory the sub-category of the post
     * @param suburb      the suburb related to the post
     * @param keywords    optional list of keywords to filter the results
     * @param page        the page number to retrieve (1-based)
     * @param size        the number of items per page
     * @return {@link PropertyPostPaginationResponse} containing paginated search results
     */
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

    /**
     * Searches for Job posts based on title, sub-category, suburb, and optional keywords.
     *
     * @param title       the post title to search
     * @param subCategory the sub-category of the post
     * @param suburb      the suburb related to the post
     * @param keywords    optional list of keywords to filter the results
     * @param page        the page number to retrieve (1-based)
     * @param size        the number of items per page
     * @return {@link JobPostPaginationResponse} containing paginated search results
     */
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

    /**
     * Searches for Transaction posts based on title, sub-category, suburb, and optional keywords.
     *
     * @param title       the post title to search
     * @param subCategory the sub-category of the post
     * @param suburb      the suburb related to the post
     * @param keywords    optional list of keywords to filter the results
     * @param page        the page number to retrieve (1-based)
     * @param size        the number of items per page
     * @return {@link TransactionPostPaginationResponse} containing paginated search results
     */
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

    /**
     * Searches for Society posts based on title, sub-category, and optional keywords.
     *
     * @param title       the post title to search
     * @param subCategory the sub-category of the post
     * @param keywords    optional list of keywords to filter the results
     * @param page        the page number to retrieve (1-based)
     * @param size        the number of items per page
     * @return {@link SocietyPostPaginationResponse} containing paginated search results
     */
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

    /**
     * Searches for Travel posts based on title, sub-category, and optional keywords.
     *
     * @param title       the post title to search
     * @param subCategory the sub-category of the post
     * @param keywords    optional list of keywords to filter the results
     * @param page        the page number to retrieve (1-based)
     * @param size        the number of items per page
     * @return {@link TravelPostPaginationResponse} containing paginated search results
     */
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

    /**
     * Searches for Study posts based on title, sub-category, and optional keywords.
     *
     * @param title       the post title to search
     * @param subCategory the sub-category of the post
     * @param keywords    optional list of keywords to filter the results
     * @param page        the page number to retrieve (1-based)
     * @param size        the number of items per page
     * @return {@link StudyPostPaginationResponse} containing paginated search results
     */
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

    /**
     * Deletes a post by its ID.
     *
     * @param postId the ID of the post to delete
     * @return {@link ResponseEntity} with a Boolean indicating whether the deletion was successful
     */
    @DeleteMapping("delete/post")
    public ResponseEntity<Boolean> removePost(@RequestParam Long postId) {
        Boolean isDeleted = postService.removePost(postId);
        return ResponseEntity.ok(isDeleted);
    }

    /**
     * Toggles the pinned status of a post for a specific user.
     *
     * @param postId the ID of the post to pin/unpin
     * @param userId the ID of the user performing the pin action
     * @return {@link ResponseEntity} with a Boolean indicating whether the update was successful
     */
    @PutMapping("pin/post")
    public ResponseEntity<Boolean> pinPost(@RequestParam Long postId, @RequestParam Long userId) {
        Boolean isPinned = pinnablePostService.updatePinStatus(postId, userId);
        return ResponseEntity.ok(isPinned);
    }

    /**
     * Increments the victory count for the specified candidate.
     *
     * @param candidateId the ID of the candidate whose victory count is to be updated
     * @return {@link ResponseEntity} with a Boolean indicating whether the update was successful
     */
    @PutMapping("add/victory")
    public ResponseEntity<Boolean> updateVictory(@RequestParam Long candidateId) {
        Boolean isUpdated = candidateService.updateVictory(candidateId);
        return ResponseEntity.ok(isUpdated);
    }
}