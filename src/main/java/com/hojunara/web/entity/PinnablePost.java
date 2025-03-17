package com.hojunara.web.entity;

import com.hojunara.web.dto.response.SummarizedPostDto;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "pinnable_post")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "post_type", discriminatorType = DiscriminatorType.STRING)
public abstract class PinnablePost extends Post {
    @Column(name = "pinned_ad_expiry")
    private Timestamp pinnedAdExpiry;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Keyword> keywords = new ArrayList<>();
}
