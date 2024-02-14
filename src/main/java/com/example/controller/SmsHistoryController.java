package com.example.controller;

import com.example.dto.SmsHistoryDTO;
import com.example.enums.ProfileRole;
import com.example.service.SmsHistoryService;
import com.example.util.HttpRequestUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Tag(name = "SmsHistory Api list", description = "Api list for SmsHistory")
@Slf4j
@RestController
@RequestMapping("/sms_history")
public class SmsHistoryController {
    @Autowired
    private SmsHistoryService smsHistoryService;

    @Operation(summary = "Api for getByPhone", description = "this api to get sms history by phone number ")
    @PostMapping("/adm")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getByPhone(@RequestBody SmsHistoryDTO dto) {
        log.info("get sms history by phone {}", dto.getPhone());
        return ResponseEntity.ok(smsHistoryService.getByPhone(dto.getPhone()));
    }

    @Operation(summary = "Api for getByDate", description = "this api to get sms history by time")
    @GetMapping("/adm/{date}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getByDate(@PathVariable("date") LocalDate date) {
        log.info("get sms history by date {}", date);
        return ResponseEntity.ok(smsHistoryService.getByDate(date));
    }

    @Operation(summary = "Api for getPagination", description = "this api to get sms history page lip")
    @GetMapping("/adm/pagination")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getPagination(@RequestParam Integer page,
                                           @RequestParam Integer size) {
        log.info("get sms history  by pagination");
        return ResponseEntity.ok(smsHistoryService.getPagination(page, size));
    }
}
