package com.hojunara.web.entity;

import com.hojunara.web.dto.response.PostDto;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.SuperBuilder;
import java.util.ArrayList;
import java.util.List;
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
    @Size(max = 80)
    private String title;

    @Column(nullable = false)
    @Size(max = 5001)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Category category;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SubCategory subCategory;

    private String contact;

    private String email;

    @Column(nullable = false)
    private Boolean isPortrait;

    @Column(nullable = false)
    private Long viewCounts;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

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

    public Post(String title, String description, Category category, SubCategory subCategory, Boolean isPortrait) {
        this.title = title;
        this.description = description;
        this.category = category;
        this.subCategory = subCategory;
        this.isPortrait = isPortrait;
    }

    public PostDto convertToPostDto(Long userId) {
        List<String> imageUrls = getImages().stream().map(Image::getUrl).collect(Collectors.toList());

        try {
            if (this instanceof PropertyPost) {
                PropertyPost propertyPost = (PropertyPost) this;
                PostDto postDto = PostDto.builder().postId(id).title(title).description(description).category(category).subCategory(subCategory).contact(contact).email(email).isPortrait(isPortrait).imageUrls(imageUrls).period(propertyPost.getPeriod()).price(propertyPost.getPrice()).userId(user.getId()).createdAt(getCreatedAt()).build();
                return postDto;
            }
            else if (this instanceof JobPost) {
            }
            else if (this instanceof TransactionPost) {
            }
            else if (this instanceof SocietyPost) {
            }
            else if (this instanceof StudyPost) {

            }
            else if (this instanceof TravelPost) {

            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }
}