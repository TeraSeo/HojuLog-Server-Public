package com.hojunara.web.entity;

import com.hojunara.web.dto.response.DetailedInquiryDto;
import com.hojunara.web.dto.response.SummarizedInquiryDto;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "inquiry")
@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Inquiry extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "inquiry_id")
    private Long id;

    @Column(nullable = false)
    @Size(max = 80)
    private String title;

    @Column(nullable = false)
    @Size(max = 5001)
    private String description;

    private String reply;

    @Column(nullable = false)
    @Builder.Default
    private Boolean isSolved = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "inquiry", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<InquiryImage> images = new ArrayList<>();

    public SummarizedInquiryDto convertToSummarizedInquiryDto() {
        SummarizedInquiryDto summarizedInquiryDto = SummarizedInquiryDto.builder().inquiryId(id).title(title).description(description).isSolved(isSolved).build();
        return summarizedInquiryDto;
    }

    public DetailedInquiryDto convertToDetailedInquiryDto() {
        List<String> imageUrls = getImages().stream().map(InquiryImage::getUrl).collect(Collectors.toList());
        DetailedInquiryDto detailedInquiryDto = DetailedInquiryDto.builder().id(id).userId(getUser().getId()).title(title).description(description).response(reply).imageUrls(imageUrls).build();
        return detailedInquiryDto;
    }
}
