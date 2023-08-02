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
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            @RequestBody OfferDto offerDto
    ) {
        service.creatOffer(itemId, username, password, offerDto);

        ResponseDto response = new ResponseDto();
        response.setMessage("구매 제안이 등록되었습니다.");
        return response;
    }

    @GetMapping
    public Page<OfferDto> readAll(
            @PathVariable("itemId") Long itemId,
            @RequestParam(value = "page", defaultValue = "0", required = false) Integer page,
            @RequestParam(value = "limit", required = false) Integer limit,
            @RequestParam("username") String username,
            @RequestParam("password") String password
    ) {
        return service.readOffers(itemId, username, password, page, limit);
    }

    @PutMapping("/{offerId}")
    public ResponseDto update(
            @PathVariable("itemId") Long itemId,
            @PathVariable("offerId") Long id,
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            @RequestBody OfferDto offerDto
    ) {
        ResponseDto response = new ResponseDto();

        if (offerDto.getSuggestedPrice() != null) {
            service.updateSuggestedPrice(itemId, id, username, password, offerDto);
            response.setMessage("제안이 수정되었습니다.");
        }

        if (offerDto.getStatus() != null) {
            if (offerDto.getStatus().equals("확정")) {
                service.updateConfirmedStatus(itemId, id, username, password, offerDto);
                response.setMessage("구매가 확정되었습니다.");
            } else {
                service.updateStatus(itemId, id, username, password, offerDto);
                response.setMessage("제안의 상태가 변경되었습니다.");
            }
        }

        return response;
    }

    @DeleteMapping("/{offerId}")
    public ResponseDto delete(
            @PathVariable("itemId") Long itemId,
            @PathVariable("offerId") Long id,
            @RequestParam("username") String username,
            @RequestParam("password") String password
    ) {
        service.deleteOffer(itemId, id, username, password);

        ResponseDto response = new ResponseDto();
        response.setMessage("제안을 삭제했습니다.");
        return response;
    }

}
