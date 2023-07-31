package com.example.mutsamarket.service;

import com.example.mutsamarket.entity.UserEntity;
import com.example.mutsamarket.exceptions.UsernameNotFoundException;
import com.example.mutsamarket.repository.CommentRepository;
import com.example.mutsamarket.repository.ItemRepository;
import com.example.mutsamarket.dto.CommentDto;
import com.example.mutsamarket.entity.CommentEntity;
import com.example.mutsamarket.entity.ItemEntity;
import com.example.mutsamarket.exceptions.CommentNotFoundException;
import com.example.mutsamarket.exceptions.ItemNotFoundException;
import com.example.mutsamarket.exceptions.PasswordNotCorrectException;
import com.example.mutsamarket.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ItemRepository itemRepository;
    private final CommentRepository commentRepository;

    public CommentDto createComment(Long itemId, String username, String password, CommentDto commentDto) {
        // 사용자 확인
        Optional<UserEntity> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isEmpty())
            throw new UsernameNotFoundException();

        UserEntity userEntity = optionalUser.get();

        if (!passwordEncoder.matches(password, userEntity.getPassword()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

        // item 확인
        Optional<ItemEntity> optionalItemEntity = itemRepository.findById(itemId);
        if (optionalItemEntity.isEmpty())
            throw new ItemNotFoundException();

        ItemEntity itemEntity= optionalItemEntity.get();

        // 새로운 comment 생성
        CommentEntity newComment = new CommentEntity();
        newComment.setItem(itemEntity);
        newComment.setContent(commentDto.getContent());
        newComment.setUser(userEntity);

        // item에 comment 추가
        itemEntity.getComments().add(newComment);
        // user에 comment 추가
        userEntity.getComments().add(newComment);

        return CommentDto.fromEntity(commentRepository.save(newComment));
    }

    public Page<CommentDto> readComments(Long itemId, Integer page, Integer limit) {
        if (page == null || limit == null) {
            page = 0;
            limit = commentRepository.findAllByItemId(itemId).toArray().length;
        }

        Pageable pageable = PageRequest.of(page, limit, Sort.by("id").ascending());
        Page<CommentEntity> commentEntityPage = commentRepository.findAllByItemId(itemId, pageable);

        return commentEntityPage.map(CommentDto::fromEntity);
    }

    public CommentDto readComment(Long id) {
        Optional<CommentEntity> optionalCommentEntity = commentRepository.findById(id);

        if (optionalCommentEntity.isEmpty())
            throw new CommentNotFoundException();

        CommentEntity commentEntity = optionalCommentEntity.get();
        return CommentDto.fromEntity(commentEntity);
    }
    //
    // public CommentDto updateComment(Long itemId, Long id, CommentDto commentDto) {
    //     Optional<CommentEntity> optionalCommentEntity = commentRepository.findById(id);
    //
    //     if (optionalCommentEntity.isEmpty())
    //         throw new CommentNotFoundException();
    //
    //     CommentEntity commentEntity = optionalCommentEntity.get();
    //
    //     if (!itemId.equals(commentEntity.getItem().getId()))
    //         throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    //
    //     if (commentEntity.getWriter().equals(commentDto.getWriter()) && commentEntity.getPassword().equals(commentDto.getPassword())) {
    //         commentEntity.setContent(commentDto.getContent());
    //
    //         return CommentDto.fromEntity(commentRepository.save(commentEntity));
    //     } else throw new PasswordNotCorrectException();
    //
    // }
    //
    // public CommentDto updateCommentReply(Long itemId, Long id, CommentDto commentDto) {
    //     // 1. item 정보 조회
    //     Optional<ItemEntity> optionalItemEntity = itemRepository.findById(itemId);
    //
    //     if (optionalItemEntity.isEmpty())
    //         throw new ItemNotFoundException();
    //
    //     ItemEntity itemEntity = optionalItemEntity.get();
    //
    //     // 2. Comment 정보 조회
    //     Optional<CommentEntity> optionalCommentEntity = commentRepository.findById(id);
    //
    //     if (optionalCommentEntity.isEmpty())
    //         throw new CommentNotFoundException();
    //
    //     CommentEntity commentEntity = optionalCommentEntity.get();
    //
    //     // 3. 해당하는 item에 대한 comment가 맞는지
    //     if (!itemId.equals(commentEntity.getItem().getId()))
    //         throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    //
    //     // 4. item 정보를 등록한 writer일 경우, 로직 실행
    //     if (itemEntity.getUser().getUsername().equals(commentDto.getWriter()) && itemEntity.getUser().getPassword().equals(commentDto.getPassword())) {
    //         commentEntity.setReply(commentDto.getReply());
    //
    //         return CommentDto.fromEntity(commentRepository.save(commentEntity));
    //
    //     } else throw new PasswordNotCorrectException();
    // }
    //
    // public void deleteComment(Long itemId, Long id, CommentDto commentDto) {
    //     Optional<CommentEntity> optionalCommentEntity = commentRepository.findById(id);
    //
    //     if (optionalCommentEntity.isEmpty())
    //         throw new CommentNotFoundException();
    //
    //     CommentEntity commentEntity = optionalCommentEntity.get();
    //
    //     if (commentEntity.getWriter().equals(commentDto.getWriter()) && commentEntity.getPassword().equals(commentDto.getPassword())) {
    //         commentRepository.deleteById(id);
    //     } else throw new PasswordNotCorrectException();
    // }

}
