package com.example.mutsamarket.controller;

import com.example.mutsamarket.service.OfferSerivce;
import com.example.mutsamarket.dto.OfferDto;
import com.example.mutsamarket.dto.ResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/items/{itemId}/offers")
public class OfferController {

    private final OfferSerivce service;

    @PostMapping
    public ResponseDto creat(
            @PathVariable("itemId") Long itemId,
            @RequestBody OfferDto offerDto
    ) {
        service.creatOffer(itemId, offerDto);

        ResponseDto response = new ResponseDto();
        response.setMessage("구매 제안이 등록되었습니다.");
        return response;
    }

    @GetMapping("/{offerId}")
    public OfferDto read(
            @PathVariable("offerId") Long id
    ) {
        return service.readOffer(id);
    }

    @GetMapping
    public Page<OfferDto> readAll(
            @PathVariable("itemId") Long itemId,
            @RequestParam(value = "page", defaultValue = "0", required = false) Integer page,
            @RequestParam(value = "limit", required = false) Integer limit,
            @RequestParam(value = "writer") String writer,
            @RequestParam(value = "password") String password
    ) {
        return service.readOffers(itemId, page, limit, writer, password);
    }

    @PutMapping("/{offerId}")
    public ResponseDto update(
            @PathVariable("itemId") Long itemId,
            @PathVariable("offerId") Long id,
            @RequestBody OfferDto offerDto
    ) {
        ResponseDto response = new ResponseDto();

        if (offerDto.getSuggestedPrice() != null) {
            service.updateSuggestedPrice(itemId, id, offerDto);
            response.setMessage("제안이 수정되었습니다.");
        }

        if (offerDto.getStatus() != null) {
            if (offerDto.getStatus().equals("확정")) {
                service.updateConfirmedStatus(itemId, id, offerDto);
                response.setMessage("구매가 확정되었습니다.");
            } else {
                service.updateStatus(itemId, id, offerDto);
                response.setMessage("제안의 상태가 변경되었습니다.");
            }
        }

        return response;
    }

    @DeleteMapping("/{offerId}")
    public ResponseDto delete(
            @PathVariable("itemId") Long itemId,
            @PathVariable("offerId") Long id,
            @RequestBody OfferDto offerDto
    ) {
        service.deleteOffer(itemId, id, offerDto);

        ResponseDto response = new ResponseDto();
        response.setMessage("제안을 삭제했습니다.");
        return response;
    }





}
