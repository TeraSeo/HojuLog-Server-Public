package com.promo.web.entity;

import com.promo.web.dto.response.PostDto;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "post")
@Getter
@Setter
@ToString
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "post_type", discriminatorType = DiscriminatorType.STRING)
@SuperBuilder
@NoArgsConstructor
public abstract class Post extends PostBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @Column(nullable = false)
    @Size(max = 41)
    private String title;

    @Column(nullable = false)
    @Size(max = 61)
    private String subtitle;

    @Column(nullable = false)
    @Size(max = 5001)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Category category;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SubCategory subCategory;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Visibility visibility;

    @Column(nullable = false)
    private Boolean isOwnWork;

    private String ownerEmail;

    @Column(nullable = false)
    private Boolean isPortrait;

    private String logoUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToMany
    @JoinTable(
            name = "post_tags",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    @Builder.Default
    private Set<Tag> tags = new HashSet<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<PostLike> likes = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<PostBookmark> bookmarks = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Image> images = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Video> videos = new ArrayList<>();

    public Post(String title, String subtitle, String description, Category category, SubCategory subCategory, Visibility visibility, Boolean isOwnWork, String ownerEmail, Boolean isPortrait) {
        this.title = title;
        this.subtitle = subtitle;
        this.description = description;
        this.category = category;
        this.subCategory = subCategory;
        this.visibility = visibility;
        this.isOwnWork = isOwnWork;
        this.ownerEmail = ownerEmail;
        this.isPortrait = isPortrait;
    }

    public PostDto convertToPostDto(Long userId) {
        List<String> imageUrls = getImages().stream().map(Image::getUrl).collect(Collectors.toList());
        List<String> postTags = tags.stream().map(Tag::getName).collect(Collectors.toList());
        List<Long> recentLaunchedPostIds = user.getPosts().stream()
                .sorted((p1, p2) -> p2.getCreatedAt().compareTo(p1.getCreatedAt()))
                .limit(3)
                .map(Post::getId)
                .collect(Collectors.toList());
        String videoUrl = this.getVideos().size() > 0 ? this.getVideos().get(0).getUrl() : "";
        List<Long> likedUserIds = getLikes().stream().map(like -> like.getUser().getId()).collect(Collectors.toList());
        Boolean isCurrentUserLiked = likedUserIds.contains(userId);

        List<Long> bookmarkedUserIds = getBookmarks().stream().map(like -> like.getUser().getId()).collect(Collectors.toList());
        Boolean isCurrentUserBookmarked = bookmarkedUserIds.contains(userId);

        Long wholeCommentsLength = Long.valueOf(comments.size());

        try {
            if (this instanceof TechnologyPost) {
                TechnologyPost technologyPost = (TechnologyPost) this;
                PostDto technologyPostDto = PostDto.builder().postId(id).title(title).subTitle(subtitle).description(description).category(Category.Technology).subCategory(subCategory).visibility(visibility.toString()).isOwnWork(isOwnWork).ownerEmail(ownerEmail).isPortrait(isPortrait).logoUrl(logoUrl).tags(postTags).likedUserCount(Long.valueOf(likes.size())).wholeCommentsLength(wholeCommentsLength).isCurrentUserLiked(isCurrentUserLiked).isCurrentUserBookmarked(isCurrentUserBookmarked).recentLaunchedPostIds(recentLaunchedPostIds).userId(user.getId()).imageUrls(imageUrls).youtubeUrl(videoUrl).playStoreUrl(technologyPost.getPlayStoreUrl()).appStoreUrl(technologyPost.getAppStoreUrl()).webUrl(technologyPost.getWebUrl()).createdAt(getCreatedAt()).build();
                return technologyPostDto;
            }
            else if (this instanceof RestaurantPost) {
                RestaurantPost restaurantPost = (RestaurantPost) this;
                PostDto restaurantPostDto = PostDto.builder().postId(id).title(title).subTitle(subtitle).description(description).category(Category.Restaurant).subCategory(subCategory).visibility(visibility.toString()).isOwnWork(isOwnWork).ownerEmail(ownerEmail).isPortrait(isPortrait).logoUrl(logoUrl).tags(postTags).likedUserCount(Long.valueOf(likes.size())).wholeCommentsLength(wholeCommentsLength).isCurrentUserLiked(isCurrentUserLiked).isCurrentUserBookmarked(isCurrentUserBookmarked).recentLaunchedPostIds(recentLaunchedPostIds).userId(user.getId()).imageUrls(imageUrls).youtubeUrl(videoUrl).webUrl(restaurantPost.getWebUrl()).location(restaurantPost.getLocationUrl()).createdAt(getCreatedAt()).build();
                return restaurantPostDto;
            }
            else if (this instanceof LifeStylePost) {
                LifeStylePost lifeStylePost = (LifeStylePost) this;
                PostDto lifestylePostDto = PostDto.builder().postId(id).title(title).subTitle(subtitle).description(description).category(Category.Lifestyle).subCategory(subCategory).visibility(visibility.toString()).isOwnWork(isOwnWork).ownerEmail(ownerEmail).isPortrait(isPortrait).logoUrl(logoUrl).tags(postTags).likedUserCount(Long.valueOf(likes.size())).wholeCommentsLength(wholeCommentsLength).isCurrentUserLiked(isCurrentUserLiked).isCurrentUserBookmarked(isCurrentUserBookmarked).recentLaunchedPostIds(recentLaunchedPostIds).userId(user.getId()).imageUrls(imageUrls).youtubeUrl(videoUrl).webUrl(lifeStylePost.getWebUrl()).createdAt(getCreatedAt()).build();
                return lifestylePostDto;
            } else if (this instanceof EducationPost) {
                EducationPost educationPost = (EducationPost) this;
                PostDto educationPostDto = PostDto.builder().postId(id).title(title).subTitle(subtitle).description(description).category(Category.Education).subCategory(subCategory).visibility(visibility.toString()).isOwnWork(isOwnWork).ownerEmail(ownerEmail).isPortrait(isPortrait).logoUrl(logoUrl).tags(postTags).likedUserCount(Long.valueOf(likes.size())).wholeCommentsLength(wholeCommentsLength).isCurrentUserLiked(isCurrentUserLiked).isCurrentUserBookmarked(isCurrentUserBookmarked).recentLaunchedPostIds(recentLaunchedPostIds).userId(user.getId()).imageUrls(imageUrls).youtubeUrl(videoUrl).webUrl(educationPost.getWebUrl()).createdAt(getCreatedAt()).build();
                return educationPostDto;
            }
            else if (this instanceof EntertainmentPost) {
                EntertainmentPost entertainmentPost = (EntertainmentPost) this;
                PostDto entertainmentPostDto = PostDto.builder().postId(id).title(title).subTitle(subtitle).description(description).category(Category.Entertainment).subCategory(subCategory).visibility(visibility.toString()).isOwnWork(isOwnWork).ownerEmail(ownerEmail).isPortrait(isPortrait).logoUrl(logoUrl).tags(postTags).likedUserCount(Long.valueOf(likes.size())).wholeCommentsLength(wholeCommentsLength).isCurrentUserLiked(isCurrentUserLiked).isCurrentUserBookmarked(isCurrentUserBookmarked).recentLaunchedPostIds(recentLaunchedPostIds).userId(user.getId()).imageUrls(imageUrls).youtubeUrl(videoUrl).webUrl(entertainmentPost.getWebUrl()).location(entertainmentPost.getLocation()).startDateTime(entertainmentPost.getStartDateTime()).endDateTime(entertainmentPost.getEndDateTime()).createdAt(getCreatedAt()).build();
                return entertainmentPostDto;
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }
}