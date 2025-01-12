package com.hojunara.web.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "viewed_users")
@Getter
@Setter
@NoArgsConstructor
public class ViewedUser {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "view_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @Column(name = "user_id", nullable = false)
    private Long userId;
}
