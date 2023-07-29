package com.example.mutsamarket.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@ToString(exclude = {"comments", "offers"})
@Data
@Entity
@Table(name = "items")
public class ItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String title;
    @NotBlank
    private String description;
    @NotNull
    private Integer minPriceWanted;
    @NotBlank
    private String writer;
    @NotBlank
    private String password;

    private String imageUrl;
    private String status;

    @OneToMany(mappedBy = "item")
    private List<CommentEntity> comments;

    @OneToMany(mappedBy = "item")
    private List<OfferEntity> offers;
}
