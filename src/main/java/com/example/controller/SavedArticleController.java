package com.example.controller;

import com.example.service.SavedArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/savedArticle")
public class SavedArticleController {
    @Autowired
    private SavedArticleService savedArticleService;
    @PostMapping("/{articleId}")
    public ResponseEntity<?> create(@PathVariable("articleId") String id) {
        return ResponseEntity.ok(savedArticleService.create(id));
    }
    @DeleteMapping("/{articleId}")
    public ResponseEntity<?> delete(@PathVariable("articleId") String id){
        return ResponseEntity.ok(savedArticleService.delete(id));
    }



}
