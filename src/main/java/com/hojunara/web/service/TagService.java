package com.hojunara.web.service;

import com.hojunara.web.entity.Tag;

import java.util.List;
import java.util.Optional;

public interface TagService {
    List<Tag> getWholeTags();

    Tag getTagById(Long id);

    Optional<Tag> getTagByName(String name);

    Tag createTag(Tag tag);

    void deleteTagById(Long id);

    void deleteTagsWithoutPosts();
}
