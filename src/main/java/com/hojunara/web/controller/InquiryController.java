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

@RestController
@RequestMapping("api/inquiry")
public class InquiryController {
    private final InquiryService inquiryService;

    @Autowired
    public InquiryController(InquiryService inquiryService) {
        this.inquiryService = inquiryService;
    }

    @GetMapping("get/specific")
    public ResponseEntity<SummarizedInquiryDto> getSpecificInquiry(@RequestParam Long inquiryId) {
        Inquiry inquiry = inquiryService.getInquiryById(inquiryId);
        SummarizedInquiryDto summarizedInquiryDto = inquiry.convertToSummarizedInquiryDto();
        return ResponseEntity.ok(summarizedInquiryDto);
    }

    @GetMapping("get/detailed")
    public ResponseEntity<DetailedInquiryDto> getDetailedInquiry(@RequestParam Long inquiryId) {
        Inquiry inquiry = inquiryService.getInquiryById(inquiryId);
        DetailedInquiryDto detailedInquiryDto = inquiry.convertToDetailedInquiryDto();
        return ResponseEntity.ok(detailedInquiryDto);
    }

    @PostMapping("create")
    public ResponseEntity<Boolean> createInquiry(
            @Valid @RequestPart InquiryDto inquiryDto,
            @RequestPart(required = false) MultipartFile[] images
    ) {
        Inquiry inquiry = inquiryService.createInquiry(inquiryDto, images);
        return ResponseEntity.ok(inquiry != null);
    }

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
