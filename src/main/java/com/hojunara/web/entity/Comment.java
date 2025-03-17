package com.hojunara.web.entity;

import com.hojunara.web.dto.response.ResponseCommentDto;
import com.hojunara.web.dto.response.SummarizedCommentDto;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "comment")
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public abstract class Comment extends PostBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "content", nullable = false, length = 1000)
    private String content;

    @OneToMany(mappedBy = "comment", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<CommentLike> likes = new ArrayList<>();

    public abstract SummarizedCommentDto convertToSummarizedCommentDto(String userId);

    public abstract ResponseCommentDto convertToResponseCommentDto(String userId);
}
