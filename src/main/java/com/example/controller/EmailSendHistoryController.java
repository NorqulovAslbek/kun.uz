package com.example.controller;

import com.example.dto.EmailSendHistoryDTO;
import com.example.service.EmailSendHistoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @GetMapping("/adm/{email}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<EmailSendHistoryDTO>> getByEmail(@PathVariable("email") String email) {
        log.info("get email history {}", email);
        return ResponseEntity.ok(emailHistoryService.getByEmail(email));
    }

    @Operation(summary = "Api for getByDate", description = "this api is used to get email history over time")
    @GetMapping("/adm/data")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<EmailSendHistoryDTO>> getByDate(@RequestParam("date") LocalDate date) {
        log.info("get email history by date {}", date);
        return ResponseEntity.ok(emailHistoryService.getByDate(date));
    }

    @Operation(summary = "Api for pagination", description = "this api is used to get email history page lip")
    @GetMapping("/adm/pagination")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PageImpl<EmailSendHistoryDTO>> pagination(@RequestParam Integer page, @RequestParam Integer size) {
        log.info("get email history  pagination");
        return ResponseEntity.ok(emailHistoryService.pagination(page, size));
    }
}
