package com.hojunara.web.service;

import com.hojunara.web.entity.Image;
import com.hojunara.web.entity.Keyword;
import com.hojunara.web.entity.Post;
import com.hojunara.web.exception.ImageNotFoundException;
import com.hojunara.web.exception.KeywordNotFoundException;
import com.hojunara.web.repository.KeywordRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class KeywordServiceImpl implements KeywordService {

    private final KeywordRepository keywordRepository;

    @Autowired
    public KeywordServiceImpl(KeywordRepository keywordRepository) {
        this.keywordRepository = keywordRepository;
    }

    @Override
    public Keyword getImageById(Long id) {
        try {
            Optional<Keyword> keyword = keywordRepository.findById(id);
            if (keyword.isPresent()) {
                log.info("Successfully got keyword by id: {}", id);
                return keyword.get();
            }
            throw new KeywordNotFoundException("Keyword not found with id: " + id);
        } catch (Exception e) {
            log.error("Failed to get keyword by id: {}", id, e);
            throw e;
        }
    }

    @Override
    public void createKeyword(String word, Post post) {
        try {
            Keyword keyword = Keyword.builder().keyWord(word).post(post).build();
            post.getKeywords().add(keyword);
            keywordRepository.save(keyword);
            log.info("Successfully created keyword");
        } catch (Exception e) {
            log.error("Failed to create keyword");
            throw e;
        }
    }
}
