package com.example.controller;

import com.example.dto.ArticleTypeDTO;
import com.example.enums.AppLanguage;
import com.example.service.ArticleTypeService;
import com.example.util.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/articleType")
public class ArticleTypeController {
    @Autowired
    private ArticleTypeService articleTypeService;

    @PostMapping("")
    public ResponseEntity<ArticleTypeDTO> create(@RequestBody ArticleTypeDTO dto,
                                                 @RequestHeader(value = "Authorization") String jwt) {
        return JWTUtil.checkRole(jwt) ? ResponseEntity.ok(articleTypeService.create(dto))
                : ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateById(@RequestParam("id") Integer id,
                                        @RequestBody ArticleTypeDTO dto,
                                        @RequestHeader(value = "Authorization", required = true) String jwt) {
        return JWTUtil.checkRole(jwt) ? ResponseEntity.ok(articleTypeService.update(id, dto))
                : ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> delete(@RequestParam("id") Integer id, @RequestHeader(value = "Authorization") String jwt) {
        return JWTUtil.checkRole(jwt) ? ResponseEntity.ok(articleTypeService.deleteById(id))
                : ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @GetMapping("/pagination")
    public ResponseEntity<PageImpl<ArticleTypeDTO>> getPagination(@RequestParam("page") Integer page,
                                                                  @RequestParam("size") Integer size,
                                                                  @RequestHeader(value = "Authorization") String jwt) {
        return JWTUtil.checkRole(jwt) ? ResponseEntity.ok(articleTypeService.getPagination(page, size))
                : ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @GetMapping("/lang")
    public ResponseEntity<List<ArticleTypeDTO>> getByLang(@RequestParam("lang") AppLanguage lang) {
        return ResponseEntity.ok(articleTypeService.getByLang(lang));
    }

}
