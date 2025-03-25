package com.hojunara.web.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "blog_content")
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class BlogContent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "content_id")
    private Long id;

    private String type;

    @Column(nullable = false)
    private Long orderIndex;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    public static List<BlogContent> convertMapToBlogContent(List<Map<String, String>> blogContents) {
        List<BlogContent> blogContentList = new ArrayList<>();
        blogContents.forEach(blogContent -> {
            if ("description".equals(blogContent.get("type"))) {
                blogContentList.add(DescriptionContent.builder()
                        .type("description")
                        .content(blogContent.get("content"))
                        .fontSize(Integer.parseInt(blogContent.getOrDefault("fontSize", "16")))
                        .fontWeight(Integer.parseInt(blogContent.getOrDefault("fontWeight", "400")))
                        .fontFamily(blogContent.getOrDefault("fontFamily", "프리텐다드"))
                        .build());
            } else if ("image".equals(blogContent.get("type"))) {
                blogContentList.add(ImageContent.builder()
                        .type("image")
                        .imageUrl(blogContent.get("imageUrl"))
                        .build());
            }
        });
        return blogContentList;
    }

    public static List<Map<String, String>> convertBlogContentToMap(List<BlogContent> blogContents) {
        List<Map<String, String>> blogContentList = new ArrayList<>();
        blogContents.forEach(blogContent -> {
            Map<String, String> map = new HashMap<>();
            if ("description".equals(blogContent.getType())) {
                DescriptionContent descriptionContent = (DescriptionContent) blogContent;
                map.put("type", "description");
                map.put("content", descriptionContent.getContent());
                map.put("fontSize", String.valueOf(descriptionContent.getFontSize()));
                map.put("fontWeight", String.valueOf(descriptionContent.getFontWeight()));
                map.put("fontFamily", String.valueOf(descriptionContent.getFontFamily()));
            } else if ("image".equals(blogContent.getType())) {
                ImageContent imageContent = (ImageContent) blogContent;
                map.put("type", "image");
                map.put("imageUrl", imageContent.getImageUrl());
            }
            blogContentList.add(map);
        });

        return blogContentList;
    }
}
