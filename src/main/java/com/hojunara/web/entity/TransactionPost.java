package com.hojunara.web.entity;

import com.hojunara.web.dto.response.SummarizedTransactionPostDto;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.util.concurrent.ThreadLocalRandom;

@Entity
@Getter
@Setter
@ToString
@SuperBuilder
@NoArgsConstructor
@DiscriminatorValue("TRANSACTION")
public class TransactionPost extends Post {
    private TransactionType transactionType;

    private PriceType priceType;

    private Long price;

    public SummarizedTransactionPostDto convertPostToSummarizedTransactionPostDto() {
        String imageUrl = getImages().stream()
                .map(Image::getUrl)
                .findFirst()
                .orElse(null);
        double randomAverageRate = Math.round(ThreadLocalRandom.current().nextDouble(4.0, 5.01) * 10.0) / 10.0;
        return SummarizedTransactionPostDto.builder().title(getTitle()).averageRate(randomAverageRate).imageUrl(imageUrl).createdAt(getCreatedAt()).build();
    }
}
