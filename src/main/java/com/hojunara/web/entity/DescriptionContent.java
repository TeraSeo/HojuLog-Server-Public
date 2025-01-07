package com.hojunara.web.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "description_content")
@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@SuperBuilder
public class DescriptionContent extends BlogContent {
    @Column(name = "content", length = 1000)
    private String content;

    private int fontSize;

    private int fontWeight;
}
