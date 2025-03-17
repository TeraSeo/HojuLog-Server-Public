package com.hojunara.web.service;

import com.hojunara.web.entity.Keyword;
import com.hojunara.web.entity.PinnablePost;
import com.hojunara.web.entity.Post;

import java.util.List;

public interface KeywordService {
    Keyword getImageById(Long id);

    void createKeyword(String word, PinnablePost post);

    Boolean updateKeyword(PinnablePost post, List<String> updatedKeywords);
}
