package com.example.mutsamarket.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity
@Table(name = "comments")
public class CommentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // @NotNull
    // private Long itemId;
    @ManyToOne
    @NotNull
    private ItemEntity item;

    @NotBlank
    private String writer;

    @NotBlank
    private String password;

    @NotBlank
    private String content;

    private String reply;
}
