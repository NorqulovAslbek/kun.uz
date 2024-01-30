package com.example.controller;

import com.example.dto.EmailHistoryDTO;
import com.example.enums.ProfileRole;
import com.example.service.EmailHistoryService;
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
public class EmailHistoryController {
    @Autowired
    private EmailHistoryService emailHistoryService;

    @GetMapping("/{email}")
    public ResponseEntity<List<EmailHistoryDTO>> getByEmail(@PathVariable("email") String email) {
        return ResponseEntity.ok(emailHistoryService.getByEmail(email));
    }

    @GetMapping("/data")
    public ResponseEntity<List<EmailHistoryDTO>> getByDate(@RequestParam("date") LocalDate date) {
        return ResponseEntity.ok(emailHistoryService.getByDate(date));
    }

    @GetMapping("/pagination/adm")
    public ResponseEntity<PageImpl<EmailHistoryDTO>> pagination(@RequestParam Integer page, @RequestParam Integer size, HttpServletRequest request) {
        HttpRequestUtil.getProfileId(request, ProfileRole.ADMIN);
        return ResponseEntity.ok(emailHistoryService.pagination(page,size));
    }
}
