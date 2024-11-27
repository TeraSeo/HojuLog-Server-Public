package com.promo.web.repository;

import com.promo.web.entity.RestaurantPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RestaurantPostRepository extends JpaRepository<RestaurantPost, Long> {
}
