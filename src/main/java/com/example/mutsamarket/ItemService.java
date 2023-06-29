package com.example.mutsamarket;

import com.example.mutsamarket.dto.ItemDto;
import com.example.mutsamarket.entity.ItemEntity;
import com.example.mutsamarket.exceptions.ItemNotFoundException;
import com.example.mutsamarket.exceptions.PasswordNotCorrectException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.nio.file.Files;
import java.util.List;
import java.util.Optional;


@Slf4j
@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository repository;

    public ItemDto createItem(ItemDto itemDto) {

        ItemEntity newItem = new ItemEntity();
        newItem.setTitle(itemDto.getTitle());
        newItem.setDescription(itemDto.getDescription());
        newItem.setMinPriceWanted(itemDto.getMinPriceWanted());
        newItem.setWriter(itemDto.getWriter());
        newItem.setImageUrl(itemDto.getImageUrl());
        newItem.setPassword(itemDto.getPassword());
        newItem.setStatus("판매중");

        return ItemDto.fromEntity(repository.save(newItem));
    }

    public ItemDto readItemById(Long id) {

        Optional<ItemEntity> optionalItem = repository.findById(id);
        if (optionalItem.isEmpty())
            throw new ItemNotFoundException();

        return ItemDto.fromEntity(optionalItem.get());
    }

    public Page<ItemDto> readItemsPage(Integer page, Integer limit) {

        if (page == null || limit == null) {
            page = 0;
            limit = repository.findAll().toArray().length;
        }

        Pageable pageable = PageRequest.of(page, limit, Sort.by("id").ascending());

        Page<ItemEntity> itemEntityPage = repository.findAll(pageable);
        Page<ItemDto> itemDtoPage = itemEntityPage.map(ItemDto::fromEntity);
        return itemDtoPage;
    }

    // public List<ItemDto> readItems() {
    //     List<ItemEntity> itemEntityList = repository.findAll();
    //     List<ItemDto> itemDtoList = new ArrayList<>();
    //
    //     for (ItemEntity itemEntity : itemEntityList) {
    //         itemDtoList.add(ItemDto.fromEntity(itemEntity));
    //     }
    //
    //     return itemDtoList;
    // }

    public ItemDto updateItem(Long id, ItemDto itemDto) {
        Optional<ItemEntity> optionalItemEntity = repository.findById(id);

        if (optionalItemEntity.isEmpty())
            throw new ItemNotFoundException();

        ItemEntity itemEntity = optionalItemEntity.get();

        if (itemEntity.getWriter().equals(itemDto.getWriter()) && itemEntity.getPassword().equals(itemDto.getPassword()) ) {
            itemEntity.setTitle(itemDto.getTitle());
            itemEntity.setDescription(itemDto.getDescription());
            itemEntity.setMinPriceWanted(itemDto.getMinPriceWanted());
            itemEntity.setWriter(itemDto.getWriter());

            return ItemDto.fromEntity(repository.save(itemEntity));
        } else throw new PasswordNotCorrectException();

    }

    public ItemDto updateItemImage(Long id, MultipartFile image, String writer, String password) {

        Optional<ItemEntity> optionalItemEntity = repository.findById(id);

        if (optionalItemEntity.isEmpty())
            throw new ItemNotFoundException();

        ItemEntity itemEntity = optionalItemEntity.get();

        if (itemEntity.getWriter().equals(writer) && itemEntity.getPassword().equals(password) ) {
            // 1. 아이템 별 저장 폴더 생성
            String imageDir = String.format("media/%d/", id);
            log.info("imageDir = " + imageDir);
            try {
                Files.createDirectories(Path.of(imageDir));
            } catch (IOException e) {
                log.error(e.getMessage());
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
            }

            // 2. 이미지 파일명 생성
            String originalFilename = image.getOriginalFilename();
            String[] filenameSplit = originalFilename.split("\\.");
            String extension = filenameSplit[filenameSplit.length -1];
            String imageFilename = "item." + extension;
            log.info("imageFilename = " + imageFilename);

            // 3. 전체 경로 생성
            String imageFullPath = imageDir + imageFilename;
            log.info("imageFullPath = " + imageFullPath);

            // 4. 이미지 저장
            try {
                image.transferTo(Path.of(imageFullPath));
            } catch (IOException e) {
                log.error(e.getMessage());
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
            }

            // 5. image 엔터티 업데이트
            log.info(String.format("/static/%d/%s", id, imageFilename));
            itemEntity.setImageUrl(String.format("/static/%d/%s", id, imageFilename));
            return ItemDto.fromEntity(repository.save(itemEntity));

        } else throw new PasswordNotCorrectException();

    }

    public void deleteItem(Long id, String writer, String password) {
        Optional<ItemEntity> optionalItemEntity = repository.findById(id);

        if (optionalItemEntity.isEmpty())
            throw new ItemNotFoundException();

        ItemEntity itemEntity = optionalItemEntity.get();

        if (itemEntity.getWriter().equals(writer) && itemEntity.getPassword().equals(password) ) {
            repository.deleteById(id);
        } else throw new PasswordNotCorrectException();

    }
}
