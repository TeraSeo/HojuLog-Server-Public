package com.hojunara.web.service;

import com.hojunara.web.entity.Tag;
import com.hojunara.web.exception.TagNotFoundException;
import com.hojunara.web.repository.TagRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;

    @Autowired
    public TagServiceImpl(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @Override
    public List<Tag> getWholeTags() {
        try {
            List<Tag> tags = tagRepository.findAll();
            log.info("Successfully got Whole Tags");
            return tags;
        } catch (Exception e) {
            log.error("Failed to get Whole Tags", e);
            throw e;
        }
    }

    @Override
    public Tag getTagById(Long id) {
        try {
            Optional<Tag> t = tagRepository.findById(id);
            if (t.isPresent()) {
                log.info("Successfully found tag with id: {}", id);
                return t.get();
            }
            throw new TagNotFoundException("Tag not found with id: " + id);
        } catch (Exception e) {
            log.error("Failed to find tag with id: {}", id, e);
            throw e;
        }
    }

    @Override
    public Optional<Tag> getTagByName(String name) {
        try {
            Optional<Tag> t = tagRepository.findByName(name);
            log.info("Successfully found tag with name: {}", name);
            return t;
        } catch (Exception e) {
            log.error("Failed to find tag with name: {}", name, e);
            throw e;
        }
    }

    @Override
    public Tag createTag(Tag tag) {
        try {
            Tag savedTag = tagRepository.save(tag);
            log.info("Successfully created tag");
            return savedTag;
        } catch (Exception e) {
            log.error("Failed to create tag", e);
            throw e;
        }
    }

    @Override
    public void deleteTagById(Long id) {
        try {
            tagRepository.deleteById(id);
            log.info("Successfully deleted tag with id: {}", id);
        } catch (Exception e) {
            log.error("Failed to delete tag with id: {}", id, e);
            throw e;
        }
    }

    @Override
    public void deleteTagsWithoutPosts() {
        try {
            List<Tag> tagsWithoutPosts = tagRepository.findTagsWithoutPosts();
            tagsWithoutPosts.forEach((tag) -> {
                tagRepository.delete(tag);
            });
            log.info("Successfully deleted tag without posts");
        } catch (Exception e) {
            log.error("Failed to delete tag without posts", e);
            throw e;
        }
    }
}
