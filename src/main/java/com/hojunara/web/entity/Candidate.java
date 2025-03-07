package com.hojunara.web.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "candidate")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Candidate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "candidate_id")
    private Long id;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "victory_count", nullable = false)
    @Builder.Default
    private int victoryCount = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "world_cup_post_id", nullable = false)
    private WorldCupPost worldCupPost;
}
