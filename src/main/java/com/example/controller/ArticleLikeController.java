package com.example.controller;

import com.example.service.ArticleLikeService;
import com.example.util.SpringSecurityUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(name = "ArticleLike Api list", description = "Api list for ArticleLike")
@RestController
@RequestMapping("/articleLike")
public class ArticleLikeController {
    @Autowired
    private ArticleLikeService articleLikeService;

    @Operation(summary = "Api for articleClickLike", description = "this is the api for clicking likes on articles")
    @PreAuthorize("hasAnyRole('ADMIN','USER','MODERATOR','PUBLISHER')")
    @PostMapping("/like/{id}")
    public ResponseEntity<?> articleClickLike(@PathVariable String id) {
        Integer profileId = SpringSecurityUtil.getCurrentUser().getId();
        return ResponseEntity.ok(articleLikeService.articleClickLike(id, profileId));
    }

    @Operation(summary = "Api for articleClickDislike", description = "this is the api to click dislike on the article")
    @PreAuthorize("hasAnyRole('ADMIN','USER','MODERATOR','PUBLISHER')")
    @PostMapping("/dislike/{id}")
    public ResponseEntity<?> articleClickDislike(@PathVariable String id) {
        Integer profileId = SpringSecurityUtil.getCurrentUser().getId();
        return ResponseEntity.ok(articleLikeService.articleClickDislike(id, profileId));
    }

    @Operation(summary = "Api for articleLikeOrDislikeRemove", description = "this api is to remove like or dislike from an article")
    @PreAuthorize("hasAnyRole('ADMIN','USER','MODERATOR','PUBLISHER')")
    @PutMapping("/remove/{id}")
    public ResponseEntity<?> articleLikeOrDislikeRemove(@PathVariable String id) {
        Integer profileId = SpringSecurityUtil.getCurrentUser().getId();
        return ResponseEntity.ok(articleLikeService.articleLikeOrDislikeRemove(id, profileId));
    }
}
