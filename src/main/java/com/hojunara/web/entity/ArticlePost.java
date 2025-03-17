package com.hojunara.web.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "article_post")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class ArticlePost extends Post {
    @Column(nullable = false, length = 10000) // Large content field
    private String content;
}
