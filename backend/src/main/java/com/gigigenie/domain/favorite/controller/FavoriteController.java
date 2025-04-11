package com.gigigenie.domain.favorite.controller;

import com.gigigenie.domain.favorite.dto.FavoriteRequest;
import com.gigigenie.domain.favorite.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/favorite")
@RestController
public class FavoriteController {
    private final FavoriteService favoriteService;

    @GetMapping("/member")
    public ResponseEntity<List<Integer>> list(@RequestParam Integer memberId) {
        List<Integer> ids = favoriteService.list(memberId);
        return ResponseEntity.ok(ids);
    }

    @PostMapping("/add")
    public void addFavorite(@RequestBody FavoriteRequest request) {
        favoriteService.addFavorite(request);
    }

    @PostMapping("/delete")
    public void deleteFavorite(@RequestBody FavoriteRequest request) {
        favoriteService.deleteFavorite(request);
    }
}
