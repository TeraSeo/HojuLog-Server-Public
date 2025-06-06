package com.hojunara.web.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "image_content")
@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@SuperBuilder
public class ImageContent extends BlogContent {
    @Column(length = 1000)
    private String imageUrl;
}
