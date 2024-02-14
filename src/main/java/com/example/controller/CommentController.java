package com.example.controller;

import com.example.dto.CommentFilterDTO;
import com.example.dto.CreateCommentDTO;
import com.example.dto.UpdateCommentDTO;
import com.example.service.CommentService;
import com.example.util.SpringSecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comment")
public class CommentController {
    @Autowired
    private CommentService commentService;

    @PostMapping("")
    @PreAuthorize("hasAnyRole('USER','ADMIN','MODERATOR','PUBLISHER')")
    public ResponseEntity<?> create(@RequestBody CreateCommentDTO dto) {
        Integer profileId = SpringSecurityUtil.getCurrentUser().getId();
        return ResponseEntity.ok(commentService.create(dto,profileId));
    }

    @PutMapping("")
    @PreAuthorize("hasAnyRole('USER','ADMIN','MODERATOR','PUBLISHER')")
    public ResponseEntity<?> update(@RequestBody UpdateCommentDTO dto) {
        return ResponseEntity.ok(commentService.update(dto));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER','MODERATOR','PUBLISHER')")
    public ResponseEntity<?> deleteUSER(@PathVariable("id") Integer commentId) {
        return ResponseEntity.ok(commentService.deleteUSER(commentId));
    }

    @PutMapping("/adm/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteADM(@PathVariable("id") Integer commentId) {
        return ResponseEntity.ok(commentService.deleteADM(commentId));
    }

    @GetMapping("/open/{id}")
    public ResponseEntity<?> getCommentListByArticleId(@PathVariable("id") String articleId) {
        return ResponseEntity.ok(commentService.getCommentListByArticleId(articleId));
    }
    @GetMapping("/adm/pagination")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getCommentList(@RequestParam Integer page,@RequestParam Integer size){
        return ResponseEntity.ok(commentService.getCommentList(page,size));
    }
    @GetMapping("/adm/filter")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?>getCommentFilter(@RequestBody CommentFilterDTO dto,
                                             @RequestParam Integer page,@RequestParam Integer size){
        return ResponseEntity.ok(commentService.filter(dto,page,size));

    }


}
