package com.example.controller;

import com.example.dto.ArticleDTO;
import com.example.dto.CreateArticleDTO;
import com.example.dto.CreateArticleIdListDTO;
import com.example.enums.ProfileRole;
import com.example.service.ArticleService;
import com.example.util.HttpRequestUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Article Api list", description = "Api list for Article")
@Slf4j
@RestController
@RequestMapping("/article")
public class ArticleController {
    @Autowired
    private ArticleService articleService;

    @Operation(summary = "Api for create", description = "this api used for create article")
    @PostMapping("")
    public ResponseEntity<?> create(@RequestBody CreateArticleDTO dto, HttpServletRequest request) {
        log.info("create article{}", dto.getTitle());
        Integer moderatorId = HttpRequestUtil.getProfileId(request, ProfileRole.MODERATOR);
        return ResponseEntity.ok(articleService.create(dto, moderatorId));
    }

    @Operation(summary = "Api for update", description = "this api used for update article")
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@RequestBody CreateArticleDTO dto,
                                    @PathVariable String id,
                                    HttpServletRequest request) {
        log.info("update article{}", dto.getTitle());
        Integer moderatorId = HttpRequestUtil.getProfileId(request, ProfileRole.MODERATOR);
        return ResponseEntity.ok(articleService.update(dto, moderatorId, id));
    }

    @Operation(summary = "Api for delete", description = "this api used for delete by id article")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable String id, HttpServletRequest request) {
        log.info("delete article by id {}", id);
        HttpRequestUtil.getProfileId(request, ProfileRole.MODERATOR);
        return ResponseEntity.ok(articleService.delete(id));
    }

    @Operation(summary = "Api for changeStatusById", description = "this api used for change status by id article")
    @GetMapping("/{id}")
    public ResponseEntity<?> changeStatusById(@PathVariable("id") String id, HttpServletRequest request) {
        log.info("get article by id {}", id);
        HttpRequestUtil.getProfileId(request, ProfileRole.PUBLISHER);
        return ResponseEntity.ok(articleService.update(id));
    }

    @Operation(summary = "Api for getLastFiveArticleByTypes", description = "Returns the last 5 published articles of the given type")
    @GetMapping("/typeId/id")
    public ResponseEntity<?> getLastFiveArticleByTypes(@RequestParam Integer id, @RequestParam Integer size) {
        log.info("get last five article by types {}", id);
        return ResponseEntity.ok(articleService.getLastFiveArticleByTypes(id, size));
    }

    @Operation(summary = "Api for getLast8ArticlesNotIncludedInList", description = "Returns the last 8 articles whose ids are not included in the provided list")
    @PostMapping("/articles")
    public ResponseEntity<List<ArticleDTO>> getLast8ArticlesNotIncludedInList(@RequestBody CreateArticleIdListDTO listDTO) {
        return ResponseEntity.ok(articleService.getLast8ArticlesNotIncludedInList(listDTO.getArticleId()));
    }

    @Operation(summary = "Api for getLast4ArticlesByTypesExceptGivenId", description = "Returns the last 4 articles of specified type excluding the given article id")
    @GetMapping("/articleId")
    public ResponseEntity<List<ArticleDTO>> getLast4ArticlesByTypesExceptGivenId(@RequestParam String articleId, @RequestParam Integer arTyId) {
        return ResponseEntity.ok(articleService.getLast4ArticlesByTypesExceptGivenId(articleId, arTyId));
    }

}
