package com.hojunara.web.service;

import com.hojunara.web.dto.request.PropertyPostDto;
import com.hojunara.web.dto.request.TransactionPostDto;
import com.hojunara.web.entity.Post;
import com.hojunara.web.entity.PropertyPost;
import com.hojunara.web.entity.TransactionPost;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface TransactionPostService {
    List<TransactionPost> getWholePosts();

    TransactionPost getPostById(Long id);

    Post createPost(TransactionPostDto transactionPostDto, MultipartFile[] images);
}
