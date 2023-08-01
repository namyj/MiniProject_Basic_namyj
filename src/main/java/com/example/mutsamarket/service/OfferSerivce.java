package com.example.mutsamarket.service;

import com.example.mutsamarket.entity.UserEntity;
import com.example.mutsamarket.exceptions.UsernameNotFoundException;
import com.example.mutsamarket.repository.ItemRepository;
import com.example.mutsamarket.repository.OfferRepository;
import com.example.mutsamarket.dto.OfferDto;
import com.example.mutsamarket.entity.ItemEntity;
import com.example.mutsamarket.entity.OfferEntity;
import com.example.mutsamarket.exceptions.ItemNotFoundException;
import com.example.mutsamarket.exceptions.OfferNotFoundException;
import com.example.mutsamarket.exceptions.PasswordNotCorrectException;
import com.example.mutsamarket.repository.UserRepository;
import lombok.NoArgsConstructor;
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

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class OfferSerivce {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ItemRepository itemRepository;
    private final OfferRepository offerRepository;

    public OfferDto creatOffer(Long itemId, String username, String password, OfferDto offerDto) {
        // 사용자 확인
        Optional<UserEntity> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isEmpty())
            throw new UsernameNotFoundException();

        UserEntity userEntity = optionalUser.get();

        if (!passwordEncoder.matches(password, userEntity.getPassword()))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        
        // item 확인
        Optional<ItemEntity> optionalItemEntity = itemRepository.findById(itemId);
        if (optionalItemEntity.isEmpty())
            throw new ItemNotFoundException();

        ItemEntity itemEntity = optionalItemEntity.get();

        // 새로운 offer 생성
        OfferEntity newOffer = new OfferEntity();
        newOffer.setItem(itemEntity);
        newOffer.setUser(userEntity);
        newOffer.setSuggestedPrice(offerDto.getSuggestedPrice());
        newOffer.setStatus("제안");

        // user의 offer 리스트에 추가
        userEntity.getOffers().add(newOffer);
        // item의 offer 리스트에 추가
        itemEntity.getOffers().add(newOffer);

        return OfferDto.fromEntity(offerRepository.save(newOffer));
    }

    // public OfferDto readOffer(Long id) {
    //     Optional<OfferEntity> optionalOfferEntity = offerRepository.findById(id);
    //
    //     if (optionalOfferEntity.isEmpty())
    //         throw new OfferNotFoundException();
    //
    //     OfferEntity offerEntity = optionalOfferEntity.get();
    //     return OfferDto.fromEntity(offerEntity);
    // }
    //
    // public OfferDto updateSuggestedPrice(Long itemId, Long id, OfferDto offerDto) {
    //
    //     Optional<OfferEntity> optionalOfferEntity = offerRepository.findById(id);
    //
    //     if (optionalOfferEntity.isEmpty())
    //         throw new OfferNotFoundException();
    //
    //     OfferEntity offerEntity = optionalOfferEntity.get();
    //     log.info(offerEntity.toString());
    //
    //     if (!itemId.equals(offerEntity.getItem().getId()))
    //         throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    //
    //     if (offerEntity.getWriter().equals(offerDto.getWriter()) && offerEntity.getPassword().equals(offerDto.getPassword())) {
    //         offerEntity.setSuggestedPrice(offerDto.getSuggestedPrice());
    //         return OfferDto.fromEntity(offerRepository.save(offerEntity));
    //     } else throw new PasswordNotCorrectException();
    // }
    //
    // public OfferDto updateStatus(Long itemId, Long id, OfferDto offerDto) {
    //
    //     // 1. item 조회
    //     Optional<ItemEntity> optionalItemEntity = itemRepository.findById(itemId);
    //     if (optionalItemEntity.isEmpty())
    //         throw new ItemNotFoundException();
    //
    //     ItemEntity itemEntity = optionalItemEntity.get();
    //     log.info(itemEntity.toString());
    //
    //     // 2. offer 조회
    //     Optional<OfferEntity> optionalOfferEntity = offerRepository.findById(id);
    //     if (optionalOfferEntity.isEmpty())
    //         throw new OfferNotFoundException();
    //
    //     OfferEntity offerEntity = optionalOfferEntity.get();
    //     log.info(offerEntity.toString());
    //
    //     // 3. item의 id와 offer의 itemId가 동일한지 확인
    //     if (!itemId.equals(offerEntity.getItem().getId()))
    //         throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    //
    //     // 4. item의 writer, password인지 확인
    //     // 5. 로직 실행
    //     if (itemEntity.getUser().getUsername().equals(offerDto.getWriter()) && itemEntity.getUser().getPassword().equals(offerDto.getPassword())) {
    //         offerEntity.setStatus(offerDto.getStatus());
    //         return OfferDto.fromEntity(offerRepository.save(offerEntity));
    //     } else throw new PasswordNotCorrectException();
    // }
    //
    // public void updateConfirmedStatus(Long itemId, Long id, OfferDto offerDto) {
    //     // 1. offer 조회
    //     Optional<OfferEntity> optionalOfferEntity = offerRepository.findById(id);
    //
    //     if (optionalOfferEntity.isEmpty())
    //         throw new OfferNotFoundException();
    //
    //     OfferEntity targetOffer = optionalOfferEntity.get();
    //     log.info(targetOffer.toString());
    //
    //     // 2. item 조회
    //     Optional<ItemEntity> optionalItemEntity = itemRepository.findById(itemId);
    //
    //     if (optionalItemEntity.isEmpty())
    //         throw new ItemNotFoundException();
    //
    //     ItemEntity itemEntity = optionalItemEntity.get();
    //
    //     if (!itemId.equals(targetOffer.getItem().getId()))
    //         throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    //
    //     // 3-1. offer status 확인
    //     if (!targetOffer.getStatus().equals("수락"))
    //         throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    //
    //     // 3-2. offer writer & password 확인
    //     if (targetOffer.getWriter().equals(offerDto.getWriter()) && targetOffer.getPassword().equals(offerDto.getPassword())) {
    //         // 4. offer status를 "확정"으로 변경
    //         targetOffer.setStatus("확정");
    //         offerRepository.save(targetOffer);
    //
    //         // 5. 나머지 offer status를 "거절"로 변경
    //         List<OfferEntity> offerEntityList = offerRepository.findByItemId(itemId);
    //
    //         for (OfferEntity otherOffer : offerEntityList) {
    //             if (otherOffer.getId().equals(id)) continue;
    //
    //             otherOffer.setStatus("거절");
    //             offerRepository.save(otherOffer);
    //         }
    //
    //         // 6. item의 status를 "판매 완료"로 변경
    //         itemEntity.setStatus("판매 완료");
    //         itemRepository.save(itemEntity);
    //
    //     } else throw new PasswordNotCorrectException();
    // }
    //
    // public void deleteOffer(Long itemId, Long id, OfferDto offerDto) {
    //     Optional<OfferEntity> optionalOfferEntity = offerRepository.findById(id);
    //     if (optionalOfferEntity.isEmpty())
    //         throw new OfferNotFoundException();
    //
    //     Optional<ItemEntity> optionalItemEntity = itemRepository.findById(itemId);
    //     if (optionalItemEntity.isEmpty())
    //         throw new ItemNotFoundException();
    //
    //     OfferEntity offerEntity = optionalOfferEntity.get();
    //     ItemEntity itemEntity = optionalItemEntity.get();
    //
    //     log.info(offerEntity.toString());
    //     log.info(itemEntity.toString());
    //
    //     if (!itemEntity.getId().equals(offerEntity.getItem().getId()))
    //         throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    //
    //     if (offerEntity.getWriter().equals(offerDto.getWriter()) && offerEntity.getPassword().equals(offerDto.getPassword())) {
    //         itemEntity.getOffers().remove(offerEntity);
    //         offerRepository.deleteById(id);
    //     } else throw new PasswordNotCorrectException();
    // }
    //
    // public Page<OfferDto> readOffers(Long itemId, Integer page, Integer limit, String writer, String password) {
    //
    //     if (limit == null) {
    //         limit = offerRepository.findByItemId(itemId).toArray().length;
    //     }
    //
    //     Pageable pageable = PageRequest.of(page, limit, Sort.by("id").ascending());
    //
    //     Optional<ItemEntity> optionalItemEntity = itemRepository.findById(itemId);
    //
    //     if (optionalItemEntity.isEmpty())
    //         throw new ItemNotFoundException();
    //
    //     ItemEntity itemEntity = optionalItemEntity.get();
    //
    //     // 1. item writer의 경우
    //     if (itemEntity.getUser().getUsername().equals(writer)) {
    //         if (!itemEntity.getUser().getPassword().equals(password))
    //             throw new PasswordNotCorrectException();
    //
    //         Page<OfferEntity> offerEntityPage = offerRepository.findByItemId(itemId, pageable);
    //
    //         return offerEntityPage.map(OfferDto::fromEntity);
    //     } else {
    //         // 2. Offer writer의 경우
    //         Page<OfferEntity> offerEntityPage = offerRepository.findByItemIdAndWriterAndPassword(itemId, writer, password, pageable);
    //
    //         return offerEntityPage.map(OfferDto::fromEntity);
    //     }
    // }
}
