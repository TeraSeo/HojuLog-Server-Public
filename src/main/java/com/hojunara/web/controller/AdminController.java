package com.hojunara.web.controller;

import com.hojunara.web.dto.request.AdminUpdateInquiryDto;
import com.hojunara.web.dto.request.AdminUpdateUserDto;
import com.hojunara.web.dto.response.*;
import com.hojunara.web.entity.Inquiry;
import com.hojunara.web.entity.Role;
import com.hojunara.web.entity.User;
import com.hojunara.web.security.provider.JwtTokenProvider;
import com.hojunara.web.service.InquiryService;
import com.hojunara.web.service.PostService;
import com.hojunara.web.service.UserService;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * REST controller that provides admin-level endpoints for managing users, inquiries, and authentication validation.
 * <p>
 * Provides endpoints for
 * <ul>
 *     <li>JWT token validation for admin role</li>
 *     <li>User and inquiry data retrieval and updates</li>
 *     <li>Pagination for user and inquiry listings</li>
 *     <li>Weekly log (point in the application) provision to users</li>
 * </ul>
 * All endpoints are prefixed with <code>/api/admin</code>.
 * </p>
 *
 * @author Taejun Seo
 */
@RestController
@RequestMapping("api/admin")
@Slf4j
public class AdminController {
    private final UserService userService;
    private final InquiryService inquiryService;
    private final JwtTokenProvider jwtTokenProvider;
    private final PostService postService;

    @Autowired
    public AdminController(UserService userService, InquiryService inquiryService, JwtTokenProvider jwtTokenProvider, PostService postService) {
        this.userService = userService;
        this.inquiryService = inquiryService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.postService = postService;
    }

    /**
     * Validates the given JWT access and refresh tokens to check if the user is an admin.
     *
     * @param accessToken the access token from the request header
     * @param refreshToken the refresh token from the request header
     * @return {@code true} if the token is valid and the user is an admin, {@code false} otherwise;
     *         400 if both tokens are missing, 401 if invalid
     */
    @PostMapping("validate/token")
    public ResponseEntity<?> validateToken(@RequestHeader(required = false) String accessToken,
                                           @RequestHeader(required = false) String refreshToken) {
        log.info("Validate JWT token");

        try {
            if (StringUtils.isBlank(accessToken) && StringUtils.isBlank(refreshToken)) {
                log.error("Both tokens are missing");
                return ResponseEntity.badRequest().body("Tokens are needed");
            }

            if (StringUtils.isNotBlank(accessToken) && jwtTokenProvider.validateToken(accessToken)) {
                Authentication authentication = jwtTokenProvider.getAuthentication(accessToken);
                SecurityContextHolder.getContext().setAuthentication(authentication);

                String email = authentication.getName();
                User user = userService.getUserByEmail(email);
                if (user.getRole().equals(Role.ADMIN)) {
                    return ResponseEntity.ok(true);
                }
                else {
                    return ResponseEntity.ok(false);
                }
            }

            return ResponseEntity.status(HttpServletResponse.SC_UNAUTHORIZED).body("Token is not valid");

        } catch (Exception e) {
            log.error("Error while validating tokens", e);
            return ResponseEntity.status(HttpServletResponse.SC_INTERNAL_SERVER_ERROR).body("Token validation failed");
        }
    }

    /**
     * Returns basic admin dashboard data with the first 5 user and inquiry IDs.
     *
     * @return an {@link AdminResponseDto} containing limited lists of user and inquiry IDs
     */
    @GetMapping("get/specific")
    public ResponseEntity<AdminResponseDto> getAdminSpecific() {
        List<Long> userIds = userService.getWholeUsers().stream().map(User::getId).limit(5).collect(Collectors.toList());
        List<Long> inquiryIds = inquiryService.getWholeInquiries().stream().map(Inquiry::getId).limit(5).collect(Collectors.toList());
        AdminResponseDto adminResponseDto = AdminResponseDto.builder().userIds(userIds).inquiryIds(inquiryIds).build();
        return ResponseEntity.ok(adminResponseDto);
    }

    /**
     * Retrieves detailed information of a user by user ID.
     *
     * @param userId the ID of the user to retrieve
     * @return a {@link NormalUserDto} with user details
     */
    @GetMapping("get/user")
    public ResponseEntity<NormalUserDto> getUserInfo(@RequestParam Long userId) {
        User user = userService.getUserById(userId);
        NormalUserDto normalUserDto = user.convertToNormalUserDto();
        return ResponseEntity.ok(normalUserDto);
    }

    /**
     * Retrieves paginated user data.
     *
     * @param page the page number (1-based)
     * @param size the number of users per page
     * @return a {@link UserPaginationResponseData} containing user list and pagination metadata
     */
    @GetMapping("get/pageable/user")
    public ResponseEntity<UserPaginationResponseData> getWholePageableUserData(@RequestParam int page, @RequestParam int size) {
        Page<User> users = userService.getWholeUserByPage(PageRequest.of(page - 1, size));
        List<NormalUserDto> userDtoList = users.getContent()
                .stream()
                .map(user -> {
                    NormalUserDto dto = user.convertToNormalUserDto();
                    return dto;
                })
                .collect(Collectors.toList());

        UserPaginationResponseData userPaginationResponseData = UserPaginationResponseData.builder().pageSize(users.getTotalPages()).currentPagePostsCount(users.getNumberOfElements()).currentPage(page).users(userDtoList).build();
        return ResponseEntity.ok(userPaginationResponseData);
    }

    /**
     * Retrieves summarized information for a specific inquiry by its ID.
     *
     * @param inquiryId the ID of the inquiry to retrieve
     * @return a {@link ResponseEntity} containing the {@link SummarizedInquiryDto} of the inquiry
     */
    @GetMapping("get/inquiry")
    public ResponseEntity<SummarizedInquiryDto> getInquiryInfo(@RequestParam Long inquiryId) {
        Inquiry inquiry = inquiryService.getInquiryById(inquiryId);
        SummarizedInquiryDto summarizedInquiryDto = inquiry.convertToSummarizedInquiryDto();
        return ResponseEntity.ok(summarizedInquiryDto);
    }

    /**
     * Retrieves detailed information for a specific inquiry by its ID.
     *
     * @param inquiryId the ID of the inquiry to retrieve
     * @return a {@link ResponseEntity} containing the {@link DetailedInquiryDto} of the inquiry
     */
    @GetMapping("get/specific/inquiry")
    public ResponseEntity<DetailedInquiryDto> getSpecificInquiry(@RequestParam Long inquiryId) {
        Inquiry inquiry = inquiryService.getInquiryById(inquiryId);
        DetailedInquiryDto detailedInquiryDto = inquiry.convertToDetailedInquiryDto();
        return ResponseEntity.ok(detailedInquiryDto);
    }

    /**
     * Retrieves paginated inquiry data.
     *
     * @param page the page number (1-based)
     * @param size the number of inquiries per page
     * @return a {@link WholeInquiryPaginationResponse} containing inquiry list and pagination metadata
     */
    @GetMapping("get/pageable/inquiry")
    public ResponseEntity<WholeInquiryPaginationResponse> getWholePageableInquiryData(@RequestParam int page, @RequestParam int size) {
        Page<Inquiry> inquiries = inquiryService.getWholeInquiriesByPage(PageRequest.of(page - 1, size));
        List<SummarizedInquiryDto> inquiryDtoList = inquiries.getContent()
                .stream()
                .map(inquiry -> inquiry.convertToSummarizedInquiryDto())
                .collect(Collectors.toList());
        WholeInquiryPaginationResponse wholeInquiryPaginationResponse = WholeInquiryPaginationResponse.builder().pageSize(inquiries.getTotalPages()).currentPagePostsCount(inquiries.getNumberOfElements()).currentPage(page).inquiries(inquiryDtoList).build();
        return ResponseEntity.ok(wholeInquiryPaginationResponse);
    }

    /**
     * Updates user information using admin privileges.
     *
     * @param adminUpdateUserDto the DTO containing fields to update
     * @return {@code true} if the update was successful
     */
    @PutMapping("update/user")
    public ResponseEntity<Boolean> updateUserData(@RequestBody AdminUpdateUserDto adminUpdateUserDto) {
        Boolean isUpdated = userService.updateUserByAdmin(adminUpdateUserDto);
        return ResponseEntity.ok(isUpdated);
    }

    /**
     * Updates an inquiry with admin-provided information.
     *
     * @param adminUpdateInquiryDto the updated inquiry data
     * @return {@code true} if the update was successful
     */
    @PutMapping("update/inquiry")
    public ResponseEntity<Boolean> updateInquiryData(@RequestBody AdminUpdateInquiryDto adminUpdateInquiryDto) {
        Boolean isUpdated = inquiryService.updateInquiry(adminUpdateInquiryDto);
        return ResponseEntity.ok(isUpdated);
    }

    /**
     * Sends weekly log rewards to the top 10 users by like count.
     *
     * @return {@code true} if logs were successfully provided
     */
    @PutMapping("provide/logs/this-week")
    public ResponseEntity<Boolean> provideLogsThisWeek() {
        List<User> users = userService.getTop10UsersByLikesThisWeek();
        Boolean isProvided = userService.provideLogThisWeek(users);
        return ResponseEntity.ok(isProvided);
    }
}
