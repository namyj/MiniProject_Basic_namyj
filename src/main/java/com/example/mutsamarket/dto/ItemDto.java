package com.example.mutsamarket.dto;

import com.example.mutsamarket.entity.ItemEntity;
import lombok.Data;

@Data
public class ItemDto {
    private Long id;
    private String title;
    private String description;
    private String imageUrl;
    private Integer minPriceWanted;
    private String writer;
    private String password;
    private String status;

    public static ItemDto fromEntity(ItemEntity itemEntity) {
        ItemDto itemDto = new ItemDto();

        itemDto.setId(itemEntity.getId());
        itemDto.setTitle(itemEntity.getTitle());
        itemDto.setDescription(itemEntity.getDescription());
        itemDto.setImageUrl(itemEntity.getImageUrl());
        itemDto.setMinPriceWanted(itemEntity.getMinPriceWanted());
        itemDto.setWriter(itemEntity.getWriter());
        itemDto.setPassword(itemEntity.getPassword());
        itemDto.setStatus(itemEntity.getStatus());

        return itemDto;
    }

}
