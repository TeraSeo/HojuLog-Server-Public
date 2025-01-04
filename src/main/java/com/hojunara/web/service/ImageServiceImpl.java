package com.hojunara.web.service;

import com.hojunara.web.entity.Image;
import com.hojunara.web.entity.NormalPost;
import com.hojunara.web.exception.ImageNotFoundException;
import com.hojunara.web.repository.ImageRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class ImageServiceImpl implements ImageService {

    private final ImageRepository imageRepository;

    @Autowired
    public ImageServiceImpl(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    @Override
    public Image getImageById(Long id) {
        try {
            Optional<Image> image = imageRepository.findById(id);
            if (image.isPresent()) {
                log.info("Successfully got image by id: {}", id);
                return image.get();
            }
            throw new ImageNotFoundException("Image not found with id: " + id);
        } catch (Exception e) {
            log.error("Failed to get image by id: {}", id, e);
            throw e;
        }
    }

    @Override
    public void createImage(String url, NormalPost post) {
        try {
            Image img = Image.builder().url(url).post(post).build();
            post.getImages().add(img);
            imageRepository.save(img);
            log.info("Successfully created image");
        } catch (Exception e) {
            log.error("Failed to create image");
            throw e;
        }
    }
}
