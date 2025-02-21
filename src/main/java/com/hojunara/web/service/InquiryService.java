package com.hojunara.web.service;

import com.hojunara.web.dto.request.AdminUpdateInquiryDto;
import com.hojunara.web.dto.request.InquiryDto;
import com.hojunara.web.entity.Inquiry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface InquiryService {
    Inquiry getInquiryById(Long id);

    List<Inquiry> getWholeInquiries();

    Page<Inquiry> getCreatedAtDescInquiriesByPage(Long userId, Pageable pageable);

    Inquiry createInquiry(InquiryDto inquiryDto, MultipartFile[] images);

    Page<Inquiry> getWholeInquiriesByPage(Pageable pageable);

    Boolean updateInquiry(AdminUpdateInquiryDto adminUpdateInquiryDto);
}
