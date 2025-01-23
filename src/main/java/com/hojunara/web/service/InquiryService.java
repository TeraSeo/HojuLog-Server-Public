package com.hojunara.web.service;

import com.hojunara.web.dto.request.InquiryDto;
import com.hojunara.web.entity.Inquiry;
import org.springframework.web.multipart.MultipartFile;

public interface InquiryService {
    Inquiry getInquiryById(Long id);

    Inquiry createInquiry(InquiryDto inquiryDto, MultipartFile[] images);
}
