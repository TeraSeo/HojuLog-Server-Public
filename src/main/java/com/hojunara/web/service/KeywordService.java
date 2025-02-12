package com.hojunara.web.service;

import com.hojunara.web.entity.Keyword;
import com.hojunara.web.entity.Post;

public interface KeywordService {
    Keyword getImageById(Long id);

    void createKeyword(String word, Post post);
}
