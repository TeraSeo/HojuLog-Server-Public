package com.promo.web.entity;

import com.promo.web.dto.response.SummarizedCommentDto;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "comment")
@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "comment_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "content", nullable = false)
    private String content;

    @OneToMany(mappedBy = "comment", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<CommentLike> likes = new ArrayList<>();

    public SummarizedCommentDto convertToSummarizedCommentDto(Long userId) {
        List<User> likedUsers = likes.stream().map(CommentLike::getUser).collect(Collectors.toList());
        List<Long> likedUserIds = likedUsers.stream().map(User::getId).collect(Collectors.toList());
        Boolean isCurrentUserLiked = likedUserIds.contains(userId);
        return SummarizedCommentDto.builder().commentId(id).content(content).isCurrentUserLiked(isCurrentUserLiked).wholeLikedUserLength(Long.valueOf(likedUsers.size())).summarizedUserDto(user.convertToSummarisedUserDto()).createdAt(getCreatedAt()).build();
    }
}
