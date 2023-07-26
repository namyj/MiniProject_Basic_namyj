package com.example.mutsamarket.repository;

import com.example.mutsamarket.entity.OfferEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OfferRepository extends JpaRepository<OfferEntity, Long> {
    List<OfferEntity> findByItemId(Long itemId);
    Page<OfferEntity> findByItemId(Long itemId, Pageable pageable);
    Page<OfferEntity> findByItemIdAndWriterAndPassword(Long itemId, String writer, String password, Pageable pageable);
}
