package com.hojunara.web.controller;

import com.hojunara.web.dto.request.InquiryDto;
import com.hojunara.web.dto.response.SummarizedInquiryDto;
import com.hojunara.web.entity.Inquiry;
import com.hojunara.web.service.InquiryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    @PostMapping("create")
    public ResponseEntity<Boolean> createInquiry(
            @Valid @RequestPart InquiryDto inquiryDto,
            @RequestPart(required = false) MultipartFile[] images
    ) {
        Inquiry inquiry = inquiryService.createInquiry(inquiryDto, images);
        return ResponseEntity.ok(inquiry != null);
    }
}
