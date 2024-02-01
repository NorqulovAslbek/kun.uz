package com.example.controller;

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

    @GetMapping("")
    public ResponseEntity<?> getByPhone(@RequestParam String phone, HttpServletRequest request) {
        HttpRequestUtil.getProfileId(request, ProfileRole.ADMIN);
        return ResponseEntity.ok(smsHistoryService.getByPhone(phone));
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
