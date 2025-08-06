package com.hojunara.web.controller;

import com.hojunara.web.dto.request.InquiryDto;
import com.hojunara.web.dto.response.DetailedInquiryDto;
import com.hojunara.web.dto.response.SummarizedInquiryDto;
import com.hojunara.web.dto.response.WholeInquiryPaginationResponse;
import com.hojunara.web.entity.Inquiry;
import com.hojunara.web.service.InquiryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

/**
 * REST controller for handling user inquiries.
 * <p>
 * Provides endpoints for creating, retrieving, and paginating inquiries.
 * All endpoints are prefixed with <code>/api/inquiry</code>.
 * </p>
 *
 * @author Taejun Seo
 */
@RestController
@RequestMapping("api/inquiry")
public class InquiryController {
    private final InquiryService inquiryService;

    @Autowired
    public InquiryController(InquiryService inquiryService) {
        this.inquiryService = inquiryService;
    }

    /**
     * Retrieves summarized information for a specific inquiry.
     *
     * @param inquiryId the ID of the inquiry
     * @return a {@link SummarizedInquiryDto} with basic inquiry info
     */
    @GetMapping("get/specific")
    public ResponseEntity<SummarizedInquiryDto> getSpecificInquiry(@RequestParam Long inquiryId) {
        Inquiry inquiry = inquiryService.getInquiryById(inquiryId);
        SummarizedInquiryDto summarizedInquiryDto = inquiry.convertToSummarizedInquiryDto();
        return ResponseEntity.ok(summarizedInquiryDto);
    }

    /**
     * Retrieves detailed information for a specific inquiry.
     *
     * @param inquiryId the ID of the inquiry
     * @return a {@link DetailedInquiryDto} with full inquiry details
     */
    @GetMapping("get/detailed")
    public ResponseEntity<DetailedInquiryDto> getDetailedInquiry(@RequestParam Long inquiryId) {
        Inquiry inquiry = inquiryService.getInquiryById(inquiryId);
        DetailedInquiryDto detailedInquiryDto = inquiry.convertToDetailedInquiryDto();
        return ResponseEntity.ok(detailedInquiryDto);
    }

    /**
     * Creates a new inquiry with optional image attachments.
     *
     * @param inquiryDto the DTO containing inquiry content
     * @param images optional image attachments
     * @return {@code true} if the inquiry was created successfully
     */
    @PostMapping("create")
    public ResponseEntity<Boolean> createInquiry(
            @Valid @RequestPart InquiryDto inquiryDto,
            @RequestPart(required = false) MultipartFile[] images
    ) {
        Inquiry inquiry = inquiryService.createInquiry(inquiryDto, images);
        return ResponseEntity.ok(inquiry != null);
    }

    /**
     * Retrieves a paginated list of inquiries created by the specified user.
     *
     * @param userId the ID of the user
     * @param page the page number (1-based)
     * @param size the number of inquiries per page
     * @return a {@link WholeInquiryPaginationResponse} with inquiry list and pagination info
     */
    @GetMapping("get/pageable/inquiries")
    public ResponseEntity<WholeInquiryPaginationResponse> getPageableInquiriesPosts(@RequestHeader int userId, @RequestParam int page, @RequestParam int size) {
        Page<Inquiry> inquiries = inquiryService.getCreatedAtDescInquiriesByPage(Long.valueOf(userId), PageRequest.of(page - 1, size));
        List<SummarizedInquiryDto> summarizedInquiryDtoList = inquiries.getContent()
                .stream()
                .map(inquiry -> inquiry.convertToSummarizedInquiryDto())
                .collect(Collectors.toList());

        WholeInquiryPaginationResponse inquiryPaginationResponse = WholeInquiryPaginationResponse.builder().pageSize(inquiries.getTotalPages()).currentPagePostsCount(inquiries.getNumberOfElements()).currentPage(page).inquiries(summarizedInquiryDtoList).build();
        return ResponseEntity.ok(inquiryPaginationResponse);
    }
}
