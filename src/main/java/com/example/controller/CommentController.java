package com.example.controller;

import com.example.dto.CommentFilterDTO;
import com.example.dto.CreateCommentDTO;
import com.example.dto.UpdateCommentDTO;
import com.example.service.CommentService;
import com.example.util.SpringSecurityUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Comment Api list", description = "Api list for Comment")
@RestController
@RequestMapping("/comment")
public class CommentController {
    @Autowired
    private CommentService commentService;

    @Operation(summary = "Api for create", description = "to comment on this api article")
    @PostMapping("")
    @PreAuthorize("hasAnyRole('USER','ADMIN','MODERATOR','PUBLISHER')")
    public ResponseEntity<?> create(@RequestBody CreateCommentDTO dto) {
        Integer profileId = SpringSecurityUtil.getCurrentUser().getId();
        return ResponseEntity.ok(commentService.create(dto, profileId));
    }

    @Operation(summary = "Api for update", description = "to modify a comment written to this api article")
    @PutMapping("")
    @PreAuthorize("hasAnyRole('USER','ADMIN','MODERATOR','PUBLISHER')")
    public ResponseEntity<?> update(@RequestBody UpdateCommentDTO dto) {
        return ResponseEntity.ok(commentService.update(dto));
    }

    @Operation(summary = "Api for deleteUSER", description = "to delete a comment on the article(user)")
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER','MODERATOR','PUBLISHER')")
    public ResponseEntity<?> deleteUSER(@PathVariable("id") Integer commentId) {
        return ResponseEntity.ok(commentService.deleteUSER(commentId));
    }

    @Operation(summary = "Api for deleteADM", description = "to delete a comment on the article(admin)")
    @PutMapping("/adm/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteADM(@PathVariable("id") Integer commentId) {
        return ResponseEntity.ok(commentService.deleteADM(commentId));
    }

    @Operation(summary = "Api for getCommentListByArticleId", description = "this api commit to get comments by id")
    @GetMapping("/open/{id}")
    public ResponseEntity<?> getCommentListByArticleId(@PathVariable("id") String articleId) {
        return ResponseEntity.ok(commentService.getCommentListByArticleId(articleId));
    }

    @Operation(summary = "Api for getCommentList", description = "this api is for getting page comments")
    @GetMapping("/adm/pagination")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getCommentList(@RequestParam Integer page, @RequestParam Integer size) {
        return ResponseEntity.ok(commentService.getCommentList(page, size));
    }

    @Operation(summary = "Api for getCommentFilter", description = "this api is to filter comments")
    @GetMapping("/adm/filter")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getCommentFilter(@RequestBody CommentFilterDTO dto,
                                              @RequestParam Integer page, @RequestParam Integer size) {
        return ResponseEntity.ok(commentService.filter(dto, page, size));
    }

    @Operation(summary = "Api for getCommentFilter", description = "this api get replied Comment List by Comment Id")
    @GetMapping("/{id}")
    public ResponseEntity<?> getRepliedCommentListByCommentId(@PathVariable Integer id) {
        return ResponseEntity.ok(commentService.getRepliedCommentListByCommentId(id));
    }


}
