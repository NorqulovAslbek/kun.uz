package com.example.controller;

import com.example.dto.*;
import com.example.enums.AppLanguage;
import com.example.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Tag(name = "Authorization Api list", description = "Api list for Authorization")
@Slf4j
@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

//    private Logger log= LoggerFactory.getLogger(AuthController.class);

    @PostMapping("/login")
    @Operation(summary = "Api for login", description = "this api used for authorization")
    public ResponseEntity<ProfileDTO> login(@RequestBody AuthDTO auth,
                                            @RequestHeader(value = "Accept-Language", defaultValue = "UZ")
                                            AppLanguage language) {
        log.trace("Login In Trace");
        log.debug("Login In Debug");
        log.info("Login {} ", auth.getEmail());
        log.warn("Login {} ", auth.getEmail());
        log.error("Login {} ", auth.getEmail());
        return ResponseEntity.ok(authService.auth(auth, language));
    }

    @Operation(summary = "Api for registration", description = "this api used for authorization")
    @PostMapping("/registration")
    public ResponseEntity<Boolean> registration(@RequestBody RegistrationDTO dto,
                                                @RequestHeader(value = "Accept-Language", defaultValue = "UZ") AppLanguage appLanguage) {
        log.info("registration {}", dto.getEmail());
        return ResponseEntity.ok(authService.registration(dto, appLanguage));
    }

    @Operation(summary = "Api for emailVerification", description = "this api used for authorization")
    @GetMapping("/verification/email/{jwt}")
    public ResponseEntity<String> emailVerification(@PathVariable("jwt") String jwt,
                                                    @RequestHeader(value = "Accept-Language", defaultValue = "UZ")
                                                    AppLanguage appLanguage) {
        log.info("emailVerification {}", jwt);
        return ResponseEntity.ok(authService.emailVerification(jwt,appLanguage));
    }

    @Operation(summary = "Api for smsVerification", description = "this api used for authorization")
    @PostMapping("/verification/phone")
    public ResponseEntity<?> smsVerification(@RequestBody SendSmsDTO dto,
                                             @RequestHeader(value = "Accept-Language", defaultValue = "UZ")
                                             AppLanguage language) {
        log.info("smsVerification {}", dto.getPhone());
        return ResponseEntity.ok(authService.smsVerification(dto.getPhone(), dto.getCode(),language));
    }


}
