package com.hojunara.web.repository;

import com.hojunara.web.entity.InquiryImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InquiryImageRepository extends JpaRepository<InquiryImage, Long> {
}
