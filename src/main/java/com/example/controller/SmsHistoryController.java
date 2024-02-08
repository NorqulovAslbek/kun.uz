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
    @PostMapping("")
    public ResponseEntity<?> getByPhone(@RequestBody SmsHistoryDTO dto, HttpServletRequest request) {
        log.info("get sms history by phone {}", dto.getPhone());
        HttpRequestUtil.getProfileId(request, ProfileRole.ADMIN);
        return ResponseEntity.ok(smsHistoryService.getByPhone(dto.getPhone()));
    }

    @Operation(summary = "Api for getByDate", description = "this api to get sms history by time")
    @GetMapping("/{date}")
    public ResponseEntity<?> getByDate(@PathVariable("date") LocalDate date, HttpServletRequest request) {
        log.info("get sms history by date {}", date);
        HttpRequestUtil.getProfileId(request, ProfileRole.ADMIN);
        return ResponseEntity.ok(smsHistoryService.getByDate(date));
    }

    @Operation(summary = "Api for getPagination", description = "this api to get sms history page lip")
    @GetMapping("/pagination")
    public ResponseEntity<?> getPagination(@RequestParam Integer page,
                                           @RequestParam Integer size,
                                           HttpServletRequest request) {
        log.info("get sms history  by pagination");
        HttpRequestUtil.getProfileId(request, ProfileRole.ADMIN);
        return ResponseEntity.ok(smsHistoryService.getPagination(page, size));
    }
}
