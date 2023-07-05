package com.example.mutsamarket;

import com.example.mutsamarket.dto.CommentDto;
import com.example.mutsamarket.entity.CommentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<CommentEntity, Long> {
    Page<CommentEntity> findAllByItemId(Long itemId, Pageable pageable);
    List<CommentEntity> findAllByItemId(Long itemId);
}
