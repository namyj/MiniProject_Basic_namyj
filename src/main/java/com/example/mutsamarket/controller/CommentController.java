package com.example.mutsamarket.controller;

import com.example.mutsamarket.service.CommentService;
import com.example.mutsamarket.dto.CommentDto;
import com.example.mutsamarket.dto.ResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/items/{itemId}/comments")
public class CommentController {

    private final CommentService service;

    @PostMapping
    public ResponseDto create(
            @PathVariable("itemId") Long itemId,
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            @RequestBody CommentDto commentDto
    ) {
        CommentDto newComment = service.createComment(itemId, username, password, commentDto);
        log.info(newComment.toString());

        ResponseDto response = new ResponseDto();
        response.setMessage("댓글이 등록되었습니다.");
        return response;
    }

    @GetMapping
    public Page<CommentDto> readAll(
            @PathVariable("itemId") Long itemId,
            @RequestParam(value = "page", defaultValue = "0", required = false) Integer page,
            @RequestParam(value = "limit", required = false) Integer limit
    ) {

        log.info(("page = " + page));
        log.info(("limit = " + limit));
        return service.readComments(itemId, page, limit);
    }

    @GetMapping("/{commentId}")
    public CommentDto read(
            @PathVariable("commentId") Long id
    ) {
        return service.readComment(id);
    }

    @PutMapping("/{commentId}")
    public ResponseDto update(
            @PathVariable("itemId") Long itemId,
            @PathVariable("commentId") Long id,
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            @RequestBody CommentDto commentDto
    ) {
        service.updateComment(itemId, id, username, password, commentDto);

        ResponseDto response = new ResponseDto();
        response.setMessage("댓글이 수정되었습니다.");
        return response;
    }

    // @PutMapping("/{commentId}/reply")
    // public ResponseDto updateReply(
    //         @PathVariable("itemId") Long itemId,
    //         @PathVariable("commentId") Long id,
    //         @RequestBody CommentDto commentDto
    // ) {
    //     service.updateCommentReply(itemId, id, commentDto);
    //
    //     ResponseDto response = new ResponseDto();
    //     response.setMessage("댓글에 답변이 추가되었습니다.");
    //     return response;
    // }


    @DeleteMapping("/{commentId}")
    public ResponseDto delete(
            @PathVariable("itemId") Long itemId,
            @PathVariable("commentId") Long id,
            @RequestParam("username") String username,
            @RequestParam("password") String password
    ) {
        service.deleteComment(itemId, id, username, password);

        ResponseDto response = new ResponseDto();
        response.setMessage("댓글을 삭제했습니다.");
        return response;
    }
}
