package com.hojunara.web.service;

import com.hojunara.web.dto.request.TransactionPostDto;
import com.hojunara.web.dto.request.UpdateTransactionPostDto;
import com.hojunara.web.entity.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface TransactionPostService {
    List<TransactionPost> getWholePosts();

    Page<TransactionPost> getCreatedAtDescPostsByPage(Pageable pageable);

    Page<TransactionPost> getCreatedAtDescPostsByPageNTransactionType(Pageable pageable, TransactionType transactionType);

    Page<TransactionPost> getCreatedAtDescPostsByPageNPriceType(Pageable pageable, PriceType priceType);

    Page<TransactionPost> getCreatedAtDescPostsByPageNTransactionTypeNPriceType(Pageable pageable, TransactionType transactionType, PriceType priceType);

    Page<TransactionPost> getCreatedAtDescPostsByPageNSubCategory(SubCategory subCategory, Pageable pageable);

    Page<TransactionPost> getCreatedAtDescPostsByPageNSubCategoryNTransactionType(SubCategory subCategory, TransactionType transactionType, Pageable pageable);

    Page<TransactionPost> getCreatedAtDescPostsByPageNSubCategoryNPriceType(SubCategory subCategory, PriceType priceType, Pageable pageable);

    Page<TransactionPost> getCreatedAtDescPostsByPageNSubCategoryNTransactionTypeNPriceType(SubCategory subCategory, TransactionType transactionType, PriceType priceType, Pageable pageable);

    List<TransactionPost> getRecent5Posts();

    TransactionPost getPostById(Long id);

    Post createPost(TransactionPostDto transactionPostDto, MultipartFile[] images);

    Post updatePost(UpdateTransactionPostDto updateTransactionPostDto, MultipartFile[] images);

    List<TransactionPost> searchTransactionPost(String title, String subCategory, String suburb, List<String> keywords);

    List<TransactionPost> searchByCategory();

    List<TransactionPost> searchByTitle(String title);

    List<TransactionPost> searchBySubCategory(SubCategory subCategory);

    List<TransactionPost> searchBySuburb(Suburb suburb);

    List<TransactionPost> searchByTitleAndSubCategory(String title, SubCategory subCategory);

    List<TransactionPost> searchByTitleAndSuburb(String title, Suburb suburb);

    List<TransactionPost> searchBySubCategoryAndSuburb(SubCategory subCategory, Suburb suburb);

    List<TransactionPost> searchByTitleAndSubCategoryAndSuburb(String title, SubCategory subCategory, Suburb suburb);

    Page<TransactionPost> convertPostsAsPage(List<TransactionPost> posts, Pageable pageable);
}
