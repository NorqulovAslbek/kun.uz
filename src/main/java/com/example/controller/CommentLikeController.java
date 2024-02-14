package com.example.controller;

import com.example.service.CommentLikeService;
import com.example.util.HttpRequestUtil;
import com.example.util.SpringSecurityUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
@Tag(name = "CommentLike Api list", description = "Api list for CommentLike")
@RestController
@RequestMapping("/commentLike")
public class CommentLikeController {
    @Autowired
    private CommentLikeService commentLikeService;

    @Operation(summary = "Api for commentLikeClick", description = "to like the comments written on this api article")
    @PostMapping("like/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','USER','MODERATOR','PUBLISHER')")
    public ResponseEntity<?> commentLikeClick(@PathVariable Integer id) {
        Integer profileId = SpringSecurityUtil.getCurrentUser().getId();
        return ResponseEntity.ok(commentLikeService.commentLikeClick(id,profileId));
    }
    @Operation(summary = "Api for commentDislikeClick", description = "this api to dislike comment")
    @PostMapping("dislike/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','USER','MODERATOR','PUBLISHER')")
    public ResponseEntity<?> commentDislikeClick(@PathVariable Integer id) {
        Integer profileId = SpringSecurityUtil.getCurrentUser().getId();
        return ResponseEntity.ok(commentLikeService.commentDislikeClick(id,profileId));
    }
    @Operation(summary = "Api for commentRemoveLike", description = "this api to delete likes or dislikes")
    @PutMapping("remove/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','USER','MODERATOR','PUBLISHER')")
    public ResponseEntity<?> commentRemoveLike(@PathVariable Integer id) {
        Integer profileId = SpringSecurityUtil.getCurrentUser().getId();
        return ResponseEntity.ok(commentLikeService.commentRemoveLike(id,profileId));
    }

}
