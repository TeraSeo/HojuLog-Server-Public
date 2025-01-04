package com.hojunara.web.entity;
import jakarta.validation.constraints.Size;
import lombok.*;
import jakarta.persistence.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "normal_post")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class NormalPost extends Post {

    @Column(nullable = false)
    @Size(max = 5001)
    private String description;

    private String contact;
    private String email;

    @Enumerated(EnumType.STRING)
    private Suburb suburb;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Image> images = new ArrayList<>();
}