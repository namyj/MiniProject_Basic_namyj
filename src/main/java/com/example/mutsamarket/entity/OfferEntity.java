package com.example.mutsamarket.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity
@Table(name = "offers")
public class OfferEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private Long itemId;

    @NotNull
    private Integer suggestedPrice;

    @NotBlank
    private String writer;

    @NotBlank
    private String password;

    private String status;

}
