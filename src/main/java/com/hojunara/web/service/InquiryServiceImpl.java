package com.hojunara.web.service;

import com.hojunara.web.aws.s3.AwsFileService;
import com.hojunara.web.dto.request.InquiryDto;
import com.hojunara.web.entity.Inquiry;
import com.hojunara.web.entity.User;
import com.hojunara.web.exception.InquiryException;
import com.hojunara.web.repository.InquiryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.Optional;

@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class InquiryServiceImpl implements InquiryService {

    private final InquiryRepository inquiryRepository;
    private final UserService userService;
    private final AwsFileService awsFileService;
    private final InquiryImageService inquiryImageService;

    @Autowired
    public InquiryServiceImpl(InquiryRepository inquiryRepository, UserService userService, AwsFileService awsFileService, InquiryImageService inquiryImageService) {
        this.inquiryRepository = inquiryRepository;
        this.userService = userService;
        this.awsFileService = awsFileService;
        this.inquiryImageService = inquiryImageService;
    }

    @Override
    public Inquiry getInquiryById(Long id) {
        try {
            Optional<Inquiry> inquiry = inquiryRepository.findById(id);
            if (inquiry.isPresent()) {
                log.info("Successfully got inquiry by id: {}", id);
                return inquiry.get();
            }
            throw new InquiryException("Inquiry not found with id: " + id);
        } catch (Exception e) {
            log.error("Failed to get inquiry by id: {}", id, e);
            throw e;
        }
    }

    @Override
    public Inquiry createInquiry(InquiryDto inquiryDto, MultipartFile[] images) {
        User user = userService.getUserById(inquiryDto.getUserId());
        try {
            Inquiry inquiry = Inquiry.builder().title(inquiryDto.getTitle()).description(inquiryDto.getDescription()).user(user).build();
            user.getInquiries().add(inquiry);
            Inquiry createdInquiry = inquiryRepository.save(inquiry);

            if (images != null) {
                Arrays.stream(images)
                        .map(image -> awsFileService.uploadPostFile(image, user.getEmail()))
                        .forEach(imageUrl -> inquiryImageService.createImage(imageUrl, createdInquiry));
            }
            log.info("Successfully created inquiry");
            return inquiry;
        } catch (Exception e) {
            log.error("Failed to create inquiry");
            throw e;
        }
    }
}
