package com.hojunara.web.service;

import com.hojunara.web.dto.request.TransactionPostDto;
import com.hojunara.web.dto.request.UpdateTransactionPostDto;
import com.hojunara.web.entity.Post;
import com.hojunara.web.entity.SubCategory;
import com.hojunara.web.entity.TransactionPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface TransactionPostService {
    List<TransactionPost> getWholePosts();

    Page<TransactionPost> getCreatedAtDescPostsByPage(Pageable pageable);

    Page<TransactionPost> getCreatedAtDescPostsByPageNSubCategory(SubCategory subCategory, Pageable pageable);

    List<TransactionPost> getRecent5Posts();

    TransactionPost getPostById(Long id);

    Post createPost(TransactionPostDto transactionPostDto, MultipartFile[] images);

    Post updatePost(UpdateTransactionPostDto updateTransactionPostDto, MultipartFile[] images);
}
