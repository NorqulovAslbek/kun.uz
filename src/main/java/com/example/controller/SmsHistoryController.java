package com.example.controller;

import com.example.dto.SmsHistoryDTO;
import com.example.enums.ProfileRole;
import com.example.service.SmsHistoryService;
import com.example.util.HttpRequestUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/sms_history")
public class SmsHistoryController {
    @Autowired
    private SmsHistoryService smsHistoryService;

    @PostMapping("")
    public ResponseEntity<?> getByPhone(@RequestBody SmsHistoryDTO dto, HttpServletRequest request) {
        HttpRequestUtil.getProfileId(request, ProfileRole.ADMIN);
        return ResponseEntity.ok(smsHistoryService.getByPhone(dto.getPhone()));
    }

    @GetMapping("/{date}")
    public ResponseEntity<?> getByDate(@PathVariable("date") LocalDate date, HttpServletRequest request) {
        HttpRequestUtil.getProfileId(request, ProfileRole.ADMIN);
        return ResponseEntity.ok(smsHistoryService.getByDate(date));
    }

    @GetMapping("/pagination")
    public ResponseEntity<?> getPagination(@RequestParam Integer page,
                                           @RequestParam Integer size,
                                           HttpServletRequest request) {
        HttpRequestUtil.getProfileId(request, ProfileRole.ADMIN);
        return ResponseEntity.ok(smsHistoryService.getPagination(page, size));
    }

}
