package com.example.controller;

import com.example.dto.EmailSendHistoryDTO;
import com.example.enums.ProfileRole;
import com.example.service.EmailSendHistoryService;
import com.example.util.HttpRequestUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/email_history")
public class EmailSendHistoryController {
    @Autowired
    private EmailSendHistoryService emailHistoryService;

    @GetMapping("/{email}")
    public ResponseEntity<List<EmailSendHistoryDTO>> getByEmail(@PathVariable("email") String email, HttpServletRequest request) {
        HttpRequestUtil.getProfileId(request, ProfileRole.ADMIN);
        return ResponseEntity.ok(emailHistoryService.getByEmail(email));
    }

    @GetMapping("/data")
    public ResponseEntity<List<EmailSendHistoryDTO>> getByDate(@RequestParam("date") LocalDate date, HttpServletRequest request) {
        HttpRequestUtil.getProfileId(request, ProfileRole.ADMIN);
        return ResponseEntity.ok(emailHistoryService.getByDate(date));
    }

    @GetMapping("/pagination/adm")
    public ResponseEntity<PageImpl<EmailSendHistoryDTO>> pagination(@RequestParam Integer page, @RequestParam Integer size, HttpServletRequest request) {
        HttpRequestUtil.getProfileId(request, ProfileRole.ADMIN);
        return ResponseEntity.ok(emailHistoryService.pagination(page,size));
    }
}
