package com.example.controller;

import com.example.enums.ProfileRole;
import com.example.service.ArticleLikeService;
import com.example.util.HttpRequestUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/articleLike")
public class ArticleLikeController {
    @Autowired
    private ArticleLikeService articleLikeService;

    @PostMapping("/like/{id}")
    public ResponseEntity<?> articleClickLike(@PathVariable String id, HttpServletRequest request) {
        Integer profileId = HttpRequestUtil.getProfileId(request, ProfileRole.MODERATOR,ProfileRole.USER,ProfileRole.ADMIN);
        return ResponseEntity.ok(articleLikeService.articleClickLike(id, profileId));
    }
    @PostMapping("/dislike/{id}")
    public ResponseEntity<?> articleClickDislike(@PathVariable String id, HttpServletRequest request) {
        Integer profileId = HttpRequestUtil.getProfileId(request, ProfileRole.MODERATOR,ProfileRole.USER,ProfileRole.ADMIN);
        return ResponseEntity.ok(articleLikeService.articleClickDislike(id, profileId));
    }

    @PutMapping("/remove/{id}")
    public ResponseEntity<?> articleLikeOrDislikeRemove(@PathVariable String id, HttpServletRequest request) {
        Integer profileId = HttpRequestUtil.getProfileId(request, ProfileRole.MODERATOR,ProfileRole.USER,ProfileRole.ADMIN);
        return ResponseEntity.ok(articleLikeService.articleLikeOrDislikeRemove(id, profileId));
    }
}
