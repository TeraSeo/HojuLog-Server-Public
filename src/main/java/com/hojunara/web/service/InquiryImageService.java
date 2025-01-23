package com.hojunara.web.service;

import com.hojunara.web.entity.Inquiry;
import com.hojunara.web.entity.InquiryImage;

public interface InquiryImageService {
    InquiryImage getImageById(Long id);

    void createImage(String url, Inquiry inquiry);
}
