package com.hojunara.web.service;

import com.hojunara.web.dto.request.UpdateWorldCupPostDto;
import com.hojunara.web.dto.request.WorldCupPostDto;
import com.hojunara.web.entity.SubCategory;
import com.hojunara.web.entity.WorldCupPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface WorldCupPostService {
    WorldCupPost getPostById(Long id);

    List<WorldCupPost> getRecent5Posts();

    Page<WorldCupPost> getCreatedAtDescPostsByPage(Pageable pageable);

    Page<WorldCupPost> getCreatedAtDescPostsByPageNSubCategory(SubCategory subCategory, Pageable pageable);

    WorldCupPost createWorldCupPost(WorldCupPostDto worldCupPostDto, MultipartFile[] images, MultipartFile coverImage);

    WorldCupPost updatePost(UpdateWorldCupPostDto updateWorldCupPostDto, MultipartFile[] images, MultipartFile coverImage);

    List<WorldCupPost> searchWorldCupPost(String title, String subCategory, List<String> keywords);

    List<WorldCupPost> searchByCategory();

    List<WorldCupPost> searchByTitle(String title);

    List<WorldCupPost> searchBySubCategory(SubCategory subCategory);

    List<WorldCupPost> searchByTitleAndSubCategory(String title, SubCategory subCategory);

    Page<WorldCupPost> convertPostsAsPage(List<WorldCupPost> posts, Pageable pageable);
}
