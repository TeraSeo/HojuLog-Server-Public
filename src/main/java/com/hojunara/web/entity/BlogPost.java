package com.hojunara.web.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "blog_post")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public abstract class BlogPost extends Post {
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<BlogContent> blogContents = new ArrayList<>();
}