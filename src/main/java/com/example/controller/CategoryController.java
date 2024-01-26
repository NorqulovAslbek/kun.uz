package com.example.controller;

import com.example.dto.category.CreateCategoryDTO;
import com.example.enums.AppLanguage;
import com.example.service.CategoryService;
import com.example.util.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @PostMapping("")
    public ResponseEntity<?> create(@RequestBody CreateCategoryDTO dto, @RequestHeader("Authorization") String jwt) {
        return JWTUtil.checkRole(jwt) ? ResponseEntity.ok(categoryService.create(dto))
                : ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @PutMapping("/id")
    public ResponseEntity<?> update(@RequestBody CreateCategoryDTO dto, @RequestParam Integer id,
                                    @RequestHeader("Authorization") String jwt) {
        return JWTUtil.checkRole(jwt) ? ResponseEntity.ok(categoryService.update(id, dto))
                : ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@RequestHeader("Authorization") String jwt,
                                    @PathVariable("id") Integer id) {
        return JWTUtil.checkRole(jwt) ? ResponseEntity.ok(categoryService.delete(id))
                : ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @GetMapping("")
    public ResponseEntity<?> getALl(@RequestHeader("Authorization") String jwt){
        return JWTUtil.checkRole(jwt) ? ResponseEntity.ok(categoryService.getAll())
                : ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @GetMapping("/lang")
    public ResponseEntity<?> getLang(@RequestHeader("Authorization") String jwt,@RequestParam AppLanguage lang){
        return JWTUtil.checkRole(jwt) ? ResponseEntity.ok(categoryService.getLang(lang))
                : ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }




}
