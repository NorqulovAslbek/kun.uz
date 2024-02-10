package com.example.controller;

import com.example.service.CommentLikeService;
import com.example.util.HttpRequestUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/commentLike")
public class CommentLikeController {
    @Autowired
    private CommentLikeService commentLikeService;

    @PostMapping("like/{id}")
    public ResponseEntity<?> commentLikeClick(@PathVariable Integer id, HttpServletRequest request) {
        Integer profileId = HttpRequestUtil.getProfileId(request);
        return ResponseEntity.ok(commentLikeService.commentLikeClick(id,profileId));
    }

    @PostMapping("dislike/{id}")
    public ResponseEntity<?> commentDislikeClick(@PathVariable Integer id, HttpServletRequest request) {
        Integer profileId = HttpRequestUtil.getProfileId(request);
        return ResponseEntity.ok(commentLikeService.commentDislikeClick(id,profileId));
    }
    @PutMapping("remove/{id}")
    public ResponseEntity<?> commentRemoveLike(@PathVariable Integer id, HttpServletRequest request) {
        Integer profileId = HttpRequestUtil.getProfileId(request);
        return ResponseEntity.ok(commentLikeService.commentRemoveLike(id,profileId));
    }

}
