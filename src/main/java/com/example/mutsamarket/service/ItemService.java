package com.example.mutsamarket.service;

import com.example.mutsamarket.entity.*;
import com.example.mutsamarket.exceptions.UsernameNotFoundException;
import com.example.mutsamarket.repository.ItemRepository;
import com.example.mutsamarket.dto.ItemDto;
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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Slf4j
@Service
@RequiredArgsConstructor
public class ItemService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ItemRepository repository;

    public ItemDto createItem(String username, String password, ItemDto itemDto) {
        // 사용자 확인
        Optional<UserEntity> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isEmpty())
            throw new UsernameNotFoundException();

        UserEntity userEntity = optionalUser.get();

        if (!passwordEncoder.matches(password, userEntity.getPassword()))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);

        // 새로운 item 생성
        ItemEntity newItem = new ItemEntity();

        newItem.setTitle(itemDto.getTitle());
        newItem.setDescription(itemDto.getDescription());
        newItem.setMinPriceWanted(itemDto.getMinPriceWanted());
        newItem.setImageUrl(itemDto.getImageUrl()); // 필수 X
        newItem.setStatus("판매중");

        newItem.setUser(userEntity);
        newItem.setComments(new ArrayList<CommentEntity>());
        newItem.setOffers(new ArrayList<OfferEntity>());

        // user의 item 리스트에 추가
        userEntity.getItems().add(newItem);

        return ItemDto.fromEntity(repository.save(newItem));
    }

    public ItemDto readItemById(Long id) {

        Optional<ItemEntity> optionalItem = repository.findById(id);
        if (optionalItem.isEmpty())
            throw new ItemNotFoundException();

        return ItemDto.fromEntity(optionalItem.get());
    }

    public Page<ItemDto> readItems(Integer page, Integer limit) {

        if (page == null || limit == null) {
            page = 0;
            limit = repository.findAll().toArray().length;
        }

        Pageable pageable = PageRequest.of(page, limit, Sort.by("id").ascending());

        Page<ItemEntity> itemEntityPage = repository.findAll(pageable);
        Page<ItemDto> itemDtoPage = itemEntityPage.map(ItemDto::fromEntity);
        return itemDtoPage;
    }


    public ItemDto updateItem(Long id, String username, String password, ItemDto itemDto) {
        Optional<ItemEntity> optionalItemEntity = repository.findById(id);
        if (optionalItemEntity.isEmpty())
            throw new ItemNotFoundException();

        ItemEntity itemEntity = optionalItemEntity.get();
        UserEntity userEntity = itemEntity.getUser();

        if (userEntity.getUsername().equals(username) && passwordEncoder.matches(password, userEntity.getPassword())) {
            itemEntity.setTitle(itemDto.getTitle());
            itemEntity.setDescription(itemDto.getDescription());
            itemEntity.setMinPriceWanted(itemDto.getMinPriceWanted());

            return ItemDto.fromEntity(repository.save(itemEntity));
        } else throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);

    }

    public ItemDto updateItemImage(Long id, String username, String password, MultipartFile image) {

        Optional<ItemEntity> optionalItemEntity = repository.findById(id);
        if (optionalItemEntity.isEmpty())
            throw new ItemNotFoundException();

        ItemEntity itemEntity = optionalItemEntity.get();
        UserEntity userEntity = itemEntity.getUser();

        if (userEntity.getUsername().equals(username) && passwordEncoder.matches(password, userEntity.getPassword()) ) {
            // 1. 아이템 별 저장 폴더 생성
            String imageDir = String.format("media/%d/", id);
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

            // 3. 전체 경로 생성
            String imageFullPath = imageDir + imageFilename;
            log.info("Saving imageFullPath = " + imageFullPath);

            // 4. 이미지 저장
            try {
                image.transferTo(Path.of(imageFullPath));
            } catch (IOException e) {
                log.error(e.getMessage());
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
            }

            // 5. image 엔터티 업데이트
            itemEntity.setImageUrl(String.format("/static/%d/%s", id, imageFilename));
            return ItemDto.fromEntity(repository.save(itemEntity));

        } else throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
    }

    public void deleteItem(Long id, String username, String password) {
        Optional<ItemEntity> optionalItemEntity = repository.findById(id);
        if (optionalItemEntity.isEmpty())
            throw new ItemNotFoundException();

        ItemEntity itemEntity = optionalItemEntity.get();
        UserEntity userEntity = itemEntity.getUser();

        if (userEntity.getUsername().equals(username) && passwordEncoder.matches(password, userEntity.getPassword())) {
            repository.deleteById(id);
        } else throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
    }
}
