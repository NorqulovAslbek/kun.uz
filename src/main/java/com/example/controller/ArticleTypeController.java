package com.example.controller;

import com.example.dto.ArticleTypeDTO;
import com.example.service.ArticleTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/articleType")
public class ArticleTypeController {
    @Autowired
    private ArticleTypeService articleTypeService;

    @PostMapping("")
    public ResponseEntity<ArticleTypeDTO> create(@RequestBody ArticleTypeDTO dto) {
        return ResponseEntity.ok(articleTypeService.create(dto));
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateById(@RequestParam("id") Integer id, @RequestBody ArticleTypeDTO dto) {
        return ResponseEntity.ok(articleTypeService.update(id, dto));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> delete(@RequestParam("id") Integer id) {
        return ResponseEntity.ok(articleTypeService.deleteById(id));
    }

    @GetMapping("/pagination")
    public ResponseEntity<PageImpl<ArticleTypeDTO>> getPagination(@RequestParam("page") Integer page, @RequestParam("size") Integer size) {
        return ResponseEntity.ok(articleTypeService.getPagination(page, size));
    }

    @GetMapping("/lang")
    public ResponseEntity<List<ArticleTypeDTO>> getByLang(@RequestParam("lang") String lang) {
        return ResponseEntity.ok(articleTypeService.getByLang(lang));
    }

}
