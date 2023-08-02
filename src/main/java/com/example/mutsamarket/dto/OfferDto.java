package com.example.mutsamarket.dto;

import com.example.mutsamarket.entity.OfferEntity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OfferDto {
    private Long id;
    private Integer suggestedPrice;
    private String status;

    public static OfferDto fromEntity(OfferEntity offerEntity) {
        OfferDto offerDto = new OfferDto();

        offerDto.setId(offerEntity.getId());
        offerDto.setSuggestedPrice(offerEntity.getSuggestedPrice());
        offerDto.setStatus(offerEntity.getStatus());

        return offerDto;
    }
}
