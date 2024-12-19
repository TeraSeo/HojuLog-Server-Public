package com.hojunara.web.service;

import com.hojunara.web.dto.request.TransactionPostDto;
import com.hojunara.web.entity.Post;
import com.hojunara.web.entity.TransactionPost;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface TransactionPostService {
    List<TransactionPost> getWholePosts();

    List<TransactionPost> getRecent5Posts();

    TransactionPost getPostById(Long id);

    Post createPost(TransactionPostDto transactionPostDto, MultipartFile[] images);
}
