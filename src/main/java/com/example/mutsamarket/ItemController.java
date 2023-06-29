package com.example.mutsamarket;

import com.example.mutsamarket.dto.ItemDto;
import com.example.mutsamarket.dto.ResponseDto;
import com.example.mutsamarket.entity.ItemEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.cache.spi.support.AbstractReadWriteAccess;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {

    private final ItemService service;

    // POST /items
    @PostMapping
    public ResponseDto create(
            @RequestBody ItemDto itemDto
    ) {
        service.createItem(itemDto);

        ResponseDto response = new ResponseDto();
        response.setMessage("등록이 완료되었습니다.");
        return response;
    }

    // GET /items?page=1&limit=20
    @GetMapping
    public Page<ItemDto> readAllPage(
            @RequestParam(value = "page", defaultValue = "0", required = false) Integer page,
            @RequestParam(value = "limit", required = false) Integer limit
    ) {
        log.info("page = " + page);
        log.info("limit = "+ limit);

        return service.readItemsPage(page, limit);
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
            @RequestBody ItemDto itemDto
    ) {

        service.updateItem(id, itemDto);

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
            @RequestParam("writer") String  writer,
            @RequestParam("password") String password
    ) {

        log.info("writer = " + writer);
        log.info("password = " + password);

        service.updateItemImage(id, multipartFile, writer, password);

        ResponseDto response = new ResponseDto();
        response.setMessage("이미지가 등록되었습니다.");
        return response;
    }

    @DeleteMapping("/{itemId}")
    public ResponseDto delete(
            @PathVariable("itemId") Long id,
            @RequestParam(value = "writer") String writer,
            @RequestParam(value = "password") String password
    ) {
        service.deleteItem(id, writer, password);

        ResponseDto response = new ResponseDto();
        response.setMessage("물품을 삭제했습니다.");
        return response;
    }

}
