package com.gigigenie.domain.favorite.controller;

import com.gigigenie.domain.favorite.dto.FavoriteRequest;
import com.gigigenie.domain.favorite.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/favorite")
@RestController
public class FavoriteController {
    private final FavoriteService favoriteService;

    @GetMapping("/member")
    public ResponseEntity<List<Integer>> list(@RequestParam Integer memberId) {
        log.info("즐겨찾기 목록 조회 요청: memberId={}", memberId);
        List<Integer> ids = favoriteService.list(memberId);
        log.info("즐겨찾기 목록 조회 결과: {}", ids);
        return ResponseEntity.ok(ids);
    }

    @PostMapping("/add")
    public void addFavorite(@RequestBody FavoriteRequest request) {
        log.info("즐겨찾기 추가 요청: {}", request);
        favoriteService.addFavorite(request);
    }

    @PostMapping("/delete")
    public void deleteFavorite(@RequestBody FavoriteRequest request) {
        log.info("즐겨찾기 삭제 요청: {}", request);
        favoriteService.deleteFavorite(request);
    }
}
