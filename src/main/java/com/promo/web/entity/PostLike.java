package com.promo.web.entity;

import jakarta.persistence.*;
import lombok.*;
import net.minidev.json.annotate.JsonIgnore;

import java.sql.Timestamp;

@Entity
@Table(name = "post_like")
@Getter
@Setter
@Builder
@ToString(exclude = {"user", "post"})
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class PostLike {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "like_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    @JsonIgnore
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;

    @Column(name = "created_at", nullable = false)
    private Timestamp createdAt;
}
