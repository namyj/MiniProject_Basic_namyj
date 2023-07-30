package com.example.mutsamarket.controller;

import com.example.mutsamarket.service.ItemService;
import com.example.mutsamarket.dto.ItemDto;
import com.example.mutsamarket.dto.ResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {

    private final ItemService service;

    // POST /items
    @PostMapping
    public ResponseDto create(
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            @RequestBody ItemDto itemDto
    ) {
        service.createItem(username, password, itemDto);

        ResponseDto response = new ResponseDto();
        response.setMessage("등록이 완료되었습니다.");
        return response;
    }

    // GET /items?page=1&limit=20
    @GetMapping
    public Page<ItemDto> readAll(
            @RequestParam(value = "page", defaultValue = "0", required = false) Integer page,
            @RequestParam(value = "limit", required = false) Integer limit
    ) {
        log.info("page = " + page);
        log.info("limit = "+ limit);

        return service.readItems(page, limit);
    }

    // GET /items/1
    @GetMapping("/{itemId}")
    public ItemDto read(
            @PathVariable("itemId") Long id
    ) {
        return service.readItemById(id);
    }

    // PUT /items/{itemId}
    @PutMapping("/{itemId}")
    public ResponseDto update(
            @PathVariable("itemId") Long id,
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            @RequestBody ItemDto itemDto
    ) {

        service.updateItem(id, username, password, itemDto);

        ResponseDto response = new ResponseDto();
        response.setMessage("물품이 수정되었습니다.");
        return response;
    }

    // PUT /items/{itemId}/image
    @PutMapping(
        value = "/{itemId}/image",
        consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseDto updateImage(
            @PathVariable("itemId") Long id,
            @RequestParam("image") MultipartFile multipartFile,
            @RequestParam("username") String username,
            @RequestParam("password") String password
    ) {

        service.updateItemImage(id, username, password, multipartFile);

        ResponseDto response = new ResponseDto();
        response.setMessage("이미지가 등록되었습니다.");
        return response;
    }

    @DeleteMapping("/{itemId}")
    public ResponseDto delete(
            @PathVariable("itemId") Long id,
            @RequestParam("username") String username,
            @RequestParam("password") String password
    ) {
        service.deleteItem(id, username, password);

        ResponseDto response = new ResponseDto();
        response.setMessage("물품을 삭제했습니다.");
        return response;
    }

}
