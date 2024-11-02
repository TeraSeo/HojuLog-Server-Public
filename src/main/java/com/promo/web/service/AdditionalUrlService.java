package com.promo.web.service;

import com.promo.web.entity.AdditionalUrl;
import com.promo.web.entity.Post;

import java.util.List;
import java.util.Optional;

public interface AdditionalUrlService {
    List<AdditionalUrl> getWholeAdditionalUrlsByPostId(Long id);

    AdditionalUrl getAdditionalUrlById(Long id);

    AdditionalUrl getAdditionalUrlByUrl(String url);

    AdditionalUrl createAdditionalUrl(AdditionalUrl additionalUrl);

    void deleteAdditionalUrlById(Long id);
}
