package com.hojunara.web.controller;

import com.hojunara.web.dto.request.AdminUpdateInquiryDto;
import com.hojunara.web.dto.request.AdminUpdateUserDto;
import com.hojunara.web.dto.request.UpdateUserDto;
import com.hojunara.web.dto.response.*;
import com.hojunara.web.entity.Inquiry;
import com.hojunara.web.entity.Role;
import com.hojunara.web.entity.User;
import com.hojunara.web.security.provider.JwtTokenProvider;
import com.hojunara.web.service.InquiryService;
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

@RestController
@RequestMapping("api/admin")
@Slf4j
public class AdminController {
    private final UserService userService;
    private final InquiryService inquiryService;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public AdminController(UserService userService, InquiryService inquiryService, JwtTokenProvider jwtTokenProvider) {
        this.userService = userService;
        this.inquiryService = inquiryService;
        this.jwtTokenProvider = jwtTokenProvider;
    }


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

    @GetMapping("get/specific")
    public ResponseEntity<AdminResponseDto> getAdminSpecific() {
        List<Long> userIds = userService.getWholeUsers().stream().map(User::getId).limit(5).collect(Collectors.toList());
        List<Long> inquiryIds = inquiryService.getWholeInquiries().stream().map(Inquiry::getId).limit(5).collect(Collectors.toList());
        AdminResponseDto adminResponseDto = AdminResponseDto.builder().userIds(userIds).inquiryIds(inquiryIds).build();
        return ResponseEntity.ok(adminResponseDto);
    }

    @GetMapping("get/user")
    public ResponseEntity<NormalUserDto> getUserInfo(@RequestParam Long userId) {
        User user = userService.getUserById(userId);
        NormalUserDto normalUserDto = user.convertToNormalUserDto();
        return ResponseEntity.ok(normalUserDto);
    }

    @GetMapping("get/pageable/user")
    public ResponseEntity<UserPaginationResponseData> getWholePageableUserData(@RequestParam int page, @RequestParam int size) {
        Page<User> users = userService.getWholeUserByPage(PageRequest.of(page - 1, size));
        List<NormalUserDto> userDtoList = users.getContent()
                .stream()
                .map(user -> user.convertToNormalUserDto())
                .collect(Collectors.toList());
        UserPaginationResponseData userPaginationResponseData = UserPaginationResponseData.builder().pageSize(users.getTotalPages()).currentPagePostsCount(users.getNumberOfElements()).currentPage(page).users(userDtoList).build();
        return ResponseEntity.ok(userPaginationResponseData);
    }

    @GetMapping("get/inquiry")
    public ResponseEntity<SummarizedInquiryDto> getInquiryInfo(@RequestParam Long inquiryId) {
        Inquiry inquiry = inquiryService.getInquiryById(inquiryId);
        SummarizedInquiryDto summarizedInquiryDto = inquiry.convertToSummarizedInquiryDto();
        return ResponseEntity.ok(summarizedInquiryDto);
    }

    @GetMapping("get/specific/inquiry")
    public ResponseEntity<DetailedInquiryDto> getSpecificInquiry(@RequestParam Long inquiryId) {
        Inquiry inquiry = inquiryService.getInquiryById(inquiryId);
        DetailedInquiryDto detailedInquiryDto = inquiry.convertToDetailedInquiryDto();
        return ResponseEntity.ok(detailedInquiryDto);
    }

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

    @PutMapping("update/user")
    public ResponseEntity<Boolean> updateUserData(@RequestBody AdminUpdateUserDto adminUpdateUserDto) {
        Boolean isUpdated = userService.updateUserByAdmin(adminUpdateUserDto);
        return ResponseEntity.ok(isUpdated);
    }

    @PutMapping("update/inquiry")
    public ResponseEntity<Boolean> updateInquiryData(@RequestBody AdminUpdateInquiryDto adminUpdateInquiryDto) {
        Boolean isUpdated = inquiryService.updateInquiry(adminUpdateInquiryDto);
        return ResponseEntity.ok(isUpdated);
    }
}
