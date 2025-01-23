package com.hojunara.web.service;

import com.hojunara.web.dto.request.InquiryDto;
import com.hojunara.web.entity.Image;
import com.hojunara.web.entity.Inquiry;
import com.hojunara.web.entity.InquiryImage;
import com.hojunara.web.entity.NormalPost;
import com.hojunara.web.exception.ImageNotFoundException;
import com.hojunara.web.exception.InquiryImageNotFoundException;
import com.hojunara.web.repository.ImageRepository;
import com.hojunara.web.repository.InquiryImageRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class InquiryImageServiceImpl implements InquiryImageService {
    private final InquiryImageRepository inquiryImageRepository;

    @Autowired
    public InquiryImageServiceImpl(InquiryImageRepository inquiryImageRepository) {
        this.inquiryImageRepository = inquiryImageRepository;
    }

    @Override
    public InquiryImage getImageById(Long id) {
        try {
            Optional<InquiryImage> image = inquiryImageRepository.findById(id);
            if (image.isPresent()) {
                log.info("Successfully got inquiry image by id: {}", id);
                return image.get();
            }
            throw new InquiryImageNotFoundException("Inquiry Image not found with id: " + id);
        } catch (Exception e) {
            log.error("Failed to get inquiry image by id: {}", id, e);
            throw e;
        }
    }

    @Override
    public void createImage(String url, Inquiry inquiry) {
        try {
            InquiryImage img = InquiryImage.builder().url(url).inquiry(inquiry).build();
            inquiry.getImages().add(img);
            inquiryImageRepository.save(img);
            log.info("Successfully created inquiry image");
        } catch (Exception e) {
            log.error("Failed to create inquiry image");
            throw e;
        }
    }
}
