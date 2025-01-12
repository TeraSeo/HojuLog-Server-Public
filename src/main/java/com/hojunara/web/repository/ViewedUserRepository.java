package com.hojunara.web.repository;

import com.hojunara.web.entity.ViewedUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ViewedUserRepository extends JpaRepository<ViewedUser, Long> {
}
