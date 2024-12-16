package com.hojunara.web.entity;

import com.hojunara.web.dto.response.PostDto;
import com.hojunara.web.dto.response.SummarizedPostDto;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.SuperBuilder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
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

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Suburb suburb;

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
        String username = user.getUsername();

        try {
            if (this instanceof PropertyPost) {
                PropertyPost propertyPost = (PropertyPost) this;
                PostDto postDto = PostDto.builder().postId(id).title(title).description(description).category(category).subCategory(subCategory).contact(contact).email(email).isPortrait(isPortrait).imageUrls(imageUrls).period(propertyPost.getPeriod()).price(propertyPost.getPrice()).address(propertyPost.getAddress()).availableTime(propertyPost.getAvailableTime()).userId(user.getId()).username(username).viewCounts(viewCounts).createdAt(getCreatedAt()).build();
                return postDto;
            }
            else if (this instanceof JobPost) {
                JobPost jobPost = (JobPost) this;
                PostDto postDto = PostDto.builder().postId(id).title(title).description(description).category(category).subCategory(subCategory).contact(contact).email(email).isPortrait(isPortrait).imageUrls(imageUrls).jobType(jobPost.getJobType()).userId(user.getId()).username(username).viewCounts(viewCounts).createdAt(getCreatedAt()).build();
                return postDto;
            }
            else if (this instanceof TransactionPost) {
                TransactionPost transactionPost = (TransactionPost) this;
                PostDto postDto = PostDto.builder().postId(id).title(title).description(description).category(category).subCategory(subCategory).contact(contact).email(email).isPortrait(isPortrait).imageUrls(imageUrls).transactionType(transactionPost.getTransactionType()).priceType(transactionPost.getPriceType()).price(transactionPost.getPrice()).userId(user.getId()).username(username).viewCounts(viewCounts).createdAt(getCreatedAt()).build();
                return postDto;
            }
            else if (this instanceof SocietyPost) {
                PostDto postDto = PostDto.builder().postId(id).title(title).description(description).category(category).subCategory(subCategory).contact(contact).email(email).isPortrait(isPortrait).imageUrls(imageUrls).userId(user.getId()).username(username).viewCounts(viewCounts).createdAt(getCreatedAt()).build();
                return postDto;
            }
            else if (this instanceof StudyPost) {
                StudyPost studyPost = (StudyPost) this;
                PostDto postDto = PostDto.builder().postId(id).title(title).description(description).category(category).subCategory(subCategory).contact(contact).email(email).isPortrait(isPortrait).imageUrls(imageUrls).school(studyPost.getSchool()).major(studyPost.getMajor()).userId(user.getId()).username(username).viewCounts(viewCounts).createdAt(getCreatedAt()).build();
                return postDto;

            }
            else if (this instanceof TravelPost) {
                TravelPost travelPost = (TravelPost) this;
                PostDto postDto = PostDto.builder().postId(id).title(title).description(description).category(category).subCategory(subCategory).contact(contact).email(email).isPortrait(isPortrait).imageUrls(imageUrls).address(travelPost.getAddress()).country(travelPost.getCountry()).userId(user.getId()).username(username).viewCounts(viewCounts).createdAt(getCreatedAt()).build();
                return postDto;
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    public SummarizedPostDto convertToSummarizedPostDto() {
        try {
            String imageUrl = images.stream()
                    .map(Image::getUrl)
                    .findFirst()
                    .orElse(null);

            double randomAverageRate = Math.round(ThreadLocalRandom.current().nextDouble(4.0, 5.01) * 10.0) / 10.0;

            return SummarizedPostDto.builder()
                    .title(title)
                    .username(user.getUsername())
                    .averageRate(randomAverageRate)
                    .imageUrl(imageUrl)
                    .build();
        } catch (Exception e) {
            return null;
        }
    }

}