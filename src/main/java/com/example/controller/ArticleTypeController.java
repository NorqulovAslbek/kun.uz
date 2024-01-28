package com.example.controller;

import com.example.dto.ArticleTypeDTO;
import com.example.enums.AppLanguage;
import com.example.enums.ProfileRole;
import com.example.service.ArticleTypeService;
import com.example.util.HttpRequestUtil;
import com.example.util.JWTUtil;
import jakarta.servlet.http.HttpServletRequest;
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

    @PostMapping("/adm")
    public ResponseEntity<ArticleTypeDTO> create(@RequestBody ArticleTypeDTO dto,
                                                 HttpServletRequest request) {
        HttpRequestUtil.getProfileId(request, ProfileRole.ADMIN);
        return ResponseEntity.ok(articleTypeService.create(dto));
    }

    @PutMapping("/adm/update")
    public ResponseEntity<?> updateById(@RequestParam("id") Integer id,
                                        @RequestBody ArticleTypeDTO dto,
                                        HttpServletRequest request) {
        HttpRequestUtil.getProfileId(request, ProfileRole.ADMIN);
        return ResponseEntity.ok(articleTypeService.update(id, dto));
    }

    @DeleteMapping("/adm/delete")
    public ResponseEntity<?> delete(@RequestParam("id") Integer id, HttpServletRequest request) {
        HttpRequestUtil.getProfileId(request, ProfileRole.ADMIN);
        return ResponseEntity.ok(articleTypeService.deleteById(id));
    }

    @GetMapping("/admin/pagination")
    public ResponseEntity<PageImpl<ArticleTypeDTO>> getPagination(@RequestParam("page") Integer page,
                                                                  @RequestParam("size") Integer size,
                                                                  HttpServletRequest request) {
        HttpRequestUtil.getProfileId(request, ProfileRole.ADMIN);
        return ResponseEntity.ok(articleTypeService.getPagination(page, size));
    }

    @GetMapping("/lang")
    public ResponseEntity<List<ArticleTypeDTO>> getByLang(@RequestParam("lang") AppLanguage lang,HttpServletRequest request) {
        HttpRequestUtil.getProfileId(request);
        return ResponseEntity.ok(articleTypeService.getByLang(lang));
    }

}
