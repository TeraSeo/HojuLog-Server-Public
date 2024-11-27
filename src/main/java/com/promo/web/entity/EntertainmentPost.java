package com.promo.web.entity;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@ToString
@SuperBuilder
@NoArgsConstructor
@DiscriminatorValue("ENTERTAINMENT")
public class EntertainmentPost extends Post {

    public EntertainmentPost(String title, String subtitle, String description, Category category, SubCategory subCategory, Visibility visibility, Boolean isOwnWork, String ownerEmail, Boolean isPortrait, String webUrl) {
        super(title, subtitle, description, category, subCategory, visibility, isOwnWork, ownerEmail, isPortrait);
        this.webUrl = webUrl;
    }

    @Column(nullable = false)
    private LocalDateTime startDateTime;

    @Column(nullable = false)
    private LocalDateTime endDateTime;

    private String location;

    @Column(nullable = false)
    private String webUrl;
}
