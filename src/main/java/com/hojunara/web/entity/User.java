package com.hojunara.web.entity;

import com.hojunara.web.dto.response.DetailedOwnUserDto;
import com.hojunara.web.dto.response.DetailedUserDto;
import com.hojunara.web.dto.response.SummarizedUserDto;
import jakarta.persistence.*;
import lombok.*;
import net.minidev.json.annotate.JsonIgnore;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "user")
@Getter
@Setter
@Builder
@ToString(exclude = {"postLikes"})
@AllArgsConstructor
@NoArgsConstructor
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(name = "profile_picture")
    private String profilePicture;

    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RegistrationMethod registrationMethod;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private Role role = Role.USER;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private EncryptionAlgorithm algorithm = EncryptionAlgorithm.BCRYPT;

    @Builder.Default
    private Boolean isLocked = false;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    @JsonIgnore
    private List<PostLike> postLikes = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    @JsonIgnore
    private List<PostBookmark> postBookmarks = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<CommentLike> commentLikes = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @Builder.Default
    private List<Notification> notifications = new ArrayList<>();

    @OneToMany(mappedBy = "follower", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Follow> followers = new ArrayList<>();

    @OneToMany(mappedBy = "followee", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Follow> followees = new ArrayList<>();

    public SummarizedUserDto convertToSummarisedUserDto() {
        return SummarizedUserDto.builder().id(id).username(username).email(email).description(description).profilePicture(profilePicture).build();
    }

    public DetailedUserDto convertToDetailedUserDto() {
        List<Long> uploadedPostIDs = posts.stream().map(Post::getId).limit(5).collect(Collectors.toList());
        return DetailedUserDto.builder().id(id).username(username).description(description).profilePicture(profilePicture).uploadedPostIds(uploadedPostIDs).build();
    }

    public DetailedOwnUserDto convertToDetailedOwnUserDto() {
        List<Long> uploadedPostIDs = posts.stream().map(Post::getId).limit(5).collect(Collectors.toList());
        List<Long> likedPostIds = postLikes.stream().map(PostLike::getPost).limit(5).map(Post::getId).collect(Collectors.toList());
        List<Long> requestedIds = new ArrayList<>();
        return DetailedOwnUserDto.builder().id(id).username(username).description(description).profilePicture(profilePicture).uploadedPostIds(uploadedPostIDs).likedPostIds(likedPostIds).requestedIds(requestedIds).build();
    }
}
