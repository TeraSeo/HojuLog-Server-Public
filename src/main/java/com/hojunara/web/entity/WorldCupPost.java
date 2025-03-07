package com.hojunara.web.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "world_cup_post")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class WorldCupPost extends Post {

    @OneToMany(mappedBy = "worldCupPost", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("id ASC") // Keep the order consistent
    @Builder.Default
    private List<Candidate> candidates = new ArrayList<>();
}
