package com.example.mutsamarket.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.ToString;

@ToString(exclude = {"user"})
@Data
@Entity
@Table(name = "offers")
public class OfferEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @NotNull
    private ItemEntity item;

    @ManyToOne
    @NotNull
    private UserEntity user;

    @NotNull
    private Integer suggestedPrice;
    private String status;
}
