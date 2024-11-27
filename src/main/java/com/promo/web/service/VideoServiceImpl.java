package com.promo.web.service;

import com.promo.web.entity.Post;
import com.promo.web.entity.Video;
import com.promo.web.exception.VideoNotFoundException;
import com.promo.web.repository.VideoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class VideoServiceImpl implements VideoService {

    private final VideoRepository videoRepository;

    @Autowired
    public VideoServiceImpl(VideoRepository videoRepository) {
        this.videoRepository = videoRepository;
    }

    @Override
    public Video getVideoById(Long id) {
        try {
            Optional<Video> video = videoRepository.findById(id);
            if (video.isPresent()) {
                log.info("Successfully got video by id: {}", id);
                return video.get();
            }
            throw new VideoNotFoundException("Video not found with id: " + id);
        } catch (Exception e) {
            log.error("Failed to get video by id: {}", id, e);
            throw e;
        }
    }

    @Override
    public void createVideo(String url, Post post) {
        try {
            Video video = Video.builder().url(url).post(post).build();
            post.getVideos().add(video);
            videoRepository.save(video);
            log.info("Successfully created video");
        } catch (Exception e) {
            log.error("Failed to create video");
            throw e;
        }
    }
}
