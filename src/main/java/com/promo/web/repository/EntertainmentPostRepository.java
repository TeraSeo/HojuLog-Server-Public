package com.promo.web.repository;

import com.promo.web.entity.EntertainmentPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EntertainmentPostRepository extends JpaRepository<EntertainmentPost, Long> {
}
