package com.example.controller;

import com.example.dto.EmailSendHistoryDTO;
import com.example.enums.ProfileRole;
import com.example.service.EmailSendHistoryService;
import com.example.util.HttpRequestUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
@Tag(name = "EmailSendHistory Api list", description = "Api list for EmailSendHistory")
@Slf4j
@RestController
@RequestMapping("/email_history")
public class EmailSendHistoryController {
    @Autowired
    private EmailSendHistoryService emailHistoryService;
    @Operation(summary = "Api for getByEmail", description = "this api is used to get email history")
    @GetMapping("/{email}")
    public ResponseEntity<List<EmailSendHistoryDTO>> getByEmail(@PathVariable("email") String email, HttpServletRequest request) {
        log.info("get email history {}", email);
        HttpRequestUtil.getProfileId(request, ProfileRole.ADMIN);
        return ResponseEntity.ok(emailHistoryService.getByEmail(email));
    }
    @Operation(summary = "Api for getByDate", description = "this api is used to get email history over time")
    @GetMapping("/data")
    public ResponseEntity<List<EmailSendHistoryDTO>> getByDate(@RequestParam("date") LocalDate date, HttpServletRequest request) {
        log.info("get email history by date {}", date);
        HttpRequestUtil.getProfileId(request, ProfileRole.ADMIN);
        return ResponseEntity.ok(emailHistoryService.getByDate(date));
    }
    @Operation(summary = "Api for pagination", description = "this api is used to get email history page lip")
    @GetMapping("/pagination/adm")
    public ResponseEntity<PageImpl<EmailSendHistoryDTO>> pagination(@RequestParam Integer page, @RequestParam Integer size, HttpServletRequest request) {
        log.info("get email history  pagination");
        HttpRequestUtil.getProfileId(request, ProfileRole.ADMIN);
        return ResponseEntity.ok(emailHistoryService.pagination(page, size));
    }
}
