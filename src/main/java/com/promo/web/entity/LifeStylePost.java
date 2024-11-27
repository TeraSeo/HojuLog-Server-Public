package com.promo.web.entity;

import jakarta.persistence.Column;
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
@DiscriminatorValue("LIFESTYLE")
public class LifeStylePost extends Post {

    public LifeStylePost(String title, String subtitle, String description, Category category, SubCategory subCategory, Visibility visibility, Boolean isOwnWork, String ownerEmail, Boolean isPortrait, String webUrl) {
        super(title, subtitle, description, category, subCategory, visibility, isOwnWork, ownerEmail, isPortrait);
        this.webUrl = webUrl;
    }

    @Column(nullable = false)
    private String webUrl;
}
