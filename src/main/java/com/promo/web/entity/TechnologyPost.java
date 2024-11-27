package com.promo.web.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@ToString
@SuperBuilder
@NoArgsConstructor
@DiscriminatorValue("TECHNOLOGY")
public class TechnologyPost extends Post {

    public TechnologyPost(String title, String subtitle, String description, Category category, SubCategory subCategory, Visibility visibility, Boolean isOwnWork, String ownerEmail, Boolean isPortrait, String playStoreUrl, String appStoreUrl, String webUrl) {
        super(title, subtitle, description, category, subCategory, visibility, isOwnWork, ownerEmail, isPortrait);
        this.playStoreUrl = playStoreUrl;
        this.appStoreUrl = appStoreUrl;
        this.webUrl = webUrl;
    }


    private String playStoreUrl;

    private String appStoreUrl;

    private String webUrl;
}
