package com.hojunara.web.service;

import com.hojunara.web.aws.s3.AwsFileService;
import com.hojunara.web.dto.request.AdminUpdateInquiryDto;
import com.hojunara.web.dto.request.InquiryDto;
import com.hojunara.web.entity.Inquiry;
import com.hojunara.web.entity.User;
import com.hojunara.web.exception.InquiryException;
import com.hojunara.web.repository.InquiryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class InquiryServiceImpl implements InquiryService {

    private final InquiryRepository inquiryRepository;
    private final UserService userService;
    private final AwsFileService awsFileService;
    private final InquiryImageService inquiryImageService;
    private final NotificationService notificationService;

    @Autowired
    public InquiryServiceImpl(InquiryRepository inquiryRepository, UserService userService, AwsFileService awsFileService, InquiryImageService inquiryImageService, NotificationService notificationService) {
        this.inquiryRepository = inquiryRepository;
        this.userService = userService;
        this.awsFileService = awsFileService;
        this.inquiryImageService = inquiryImageService;
        this.notificationService = notificationService;
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
    public List<Inquiry> getWholeInquiries() {
        try {
            List<Inquiry> inquiries = inquiryRepository.findAll();
            log.info("Successfully fot whole inquiries");
            return  inquiries;
        } catch (Exception e) {
            log.error("Failed to get whole inquiries", e);
            throw e;
        }
    }

    @Override
    public Page<Inquiry> getCreatedAtDescInquiriesByPage(Long userId, Pageable pageable) {
        try {
            Page<Inquiry> posts = inquiryRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable);
            log.info("Successfully got pageable Inquiry order by createdAt Desc");
            return posts;
        } catch (Exception e) {
            log.error("Failed to get pageable Inquiry order by createdAt Desc", e);
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

    @Override
    public Page<Inquiry> getWholeInquiriesByPage(Pageable pageable) {
        try {
            Page<Inquiry> inquiries = inquiryRepository.findAllByOrderByCreatedAtDesc(pageable);
            log.info("Successfully got whole inquiries by page");
            return inquiries;
        } catch (Exception e) {
            log.error("Failed to get whole inquiries by page", e);
            throw e;
        }
    }

    @Override
    public Boolean updateInquiry(AdminUpdateInquiryDto adminUpdateInquiryDto) {
        Long inquiryId = adminUpdateInquiryDto.getInquiryId();
        Inquiry existingInquiry = getInquiryById(inquiryId);
        try {
            Boolean isUpdated = false;
            if (!Objects.equals(existingInquiry.getReply(), adminUpdateInquiryDto.getResponse())) {
                existingInquiry.setReply(adminUpdateInquiryDto.getResponse());
                existingInquiry.setIsSolved(true);

                String message = String.format("'%s' 문의가 해결됐습니다!", existingInquiry.getTitle());
                notificationService.createNotification("문의 알림", message, existingInquiry.getUser());
                isUpdated = true;
            }

            if (isUpdated) inquiryRepository.save(existingInquiry);

            log.info("Successfully updated inquiry with id: {}", inquiryId);

            return true;
        } catch (Exception e) {
            log.error("Failed to update inquiry with id: {}", inquiryId, e);
            throw e;
        }
    }
}
