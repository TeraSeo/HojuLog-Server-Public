package com.promo.web.repository;

import com.promo.web.entity.AdditionalUrl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AdditionalUrlRepository extends JpaRepository<AdditionalUrl, Long> {
    Optional<AdditionalUrl> findByUrl(String url);

    List<AdditionalUrl> findByPostId(Long postId);
}
