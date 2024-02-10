package com.example.controller;

import com.example.enums.ProfileRole;
import com.example.service.ArticleLikeService;
import com.example.util.HttpRequestUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @PostMapping("/remove/{id}")
    public ResponseEntity<?> articleLikeOrDislikeRemove(@PathVariable String id, HttpServletRequest request) {
        Integer profileId = HttpRequestUtil.getProfileId(request, ProfileRole.MODERATOR,ProfileRole.USER,ProfileRole.ADMIN);
        return ResponseEntity.ok(articleLikeService.articleLikeOrDislikeRemove(id, profileId));
    }
}
