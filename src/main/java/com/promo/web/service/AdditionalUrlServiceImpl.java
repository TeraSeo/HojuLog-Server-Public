package com.promo.web.service;

import com.promo.web.entity.AdditionalUrl;
import com.promo.web.exception.AdditionalUrlNotFoundException;
import com.promo.web.repository.AdditionalUrlRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class AdditionalUrlServiceImpl implements AdditionalUrlService {

    private final AdditionalUrlRepository additionalUrlRepository;

    @Autowired
    public AdditionalUrlServiceImpl(AdditionalUrlRepository additionalUrlRepository) {
        this.additionalUrlRepository = additionalUrlRepository;
    }

    @Override
    public List<AdditionalUrl> getWholeAdditionalUrlsByPostId(Long id) {
        try {
            List<AdditionalUrl> additionalUrls = additionalUrlRepository.findByPostId(id);
            log.info("Successfully got Whole Additional Urls");
            return additionalUrls;
        } catch (Exception e) {
            log.error("Failed to get Whole Additional Urls");
            throw e;
        }
    }

    @Override
    public AdditionalUrl getAdditionalUrlById(Long id) {
        try {
            Optional<AdditionalUrl> a = additionalUrlRepository.findById(id);
            if (a.isPresent()) {
                log.info("Successfully found additional url with id: {}", id);
                return a.get();
            }
            throw new AdditionalUrlNotFoundException("AdditionalUrl not found with id: " + id);
        } catch (Exception e) {
            log.error("Failed to find additional url with id: {}", id);
            throw e;
        }
    }

    @Override
    public AdditionalUrl getAdditionalUrlByUrl(String url) {
        try {
            Optional<AdditionalUrl> a = additionalUrlRepository.findByUrl(url);
            if (a.isPresent()) {
                log.info("Successfully found additional with url: {}", url);
                return a.get();
            }
            throw new AdditionalUrlNotFoundException("AdditionalUrl not found with url: " + url);
        } catch (Exception e) {
            log.error("Failed to find additional with url: {}", url);
            throw e;
        }
    }

    @Override
    public AdditionalUrl createAdditionalUrl(AdditionalUrl additionalUrl) {
        try {
            AdditionalUrl savedAdditionalUrl = additionalUrlRepository.save(additionalUrl);
            log.info("Successfully created additional url");
            return savedAdditionalUrl;
        } catch (Exception e) {
            log.error("Failed to create additional url\n" + e);
            throw e;
        }
    }

    @Override
    public void deleteAdditionalUrlById(Long id) {
        try {
            additionalUrlRepository.deleteById(id);
            log.info("Successfully delete additional url with id: {}", id);
        } catch (Exception e) {
            log.error("Failed to delete additional url with id: {}\n{}", id, e);
            throw e;
        }
    }
}
