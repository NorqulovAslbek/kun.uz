package com.example.controller;

import com.example.dto.AttachDTO;
import com.example.enums.ProfileRole;
import com.example.service.AttachService;
import com.example.util.HttpRequestUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;

@Tag(name = "Attach Api list", description = "Api list for attach")
@Slf4j
@RestController
@RequestMapping("/attach")
public class AttachController {
    @Autowired
    private AttachService attachService;

    @Operation(summary = "Api for upload", description = "this api used for upload attach")
    @PostMapping("/upload")
    public ResponseEntity<AttachDTO> upload(@RequestParam("file") MultipartFile file) {
        log.info("create attach {}",file);
        AttachDTO dto = attachService.save(file);
        return ResponseEntity.ok().body(dto);
    }

    @Operation(summary = "Api for open", description = "this api used to open image")
    @GetMapping(value = "/open/{fileName}", produces = MediaType.IMAGE_PNG_VALUE)
    public byte[] open(@PathVariable("fileName") String fileName) {
        log.info("get attach {}",fileName);
        if (fileName != null && fileName.length() > 0) {
            try {
                return this.attachService.loadImage(fileName);
            } catch (Exception e) {
                e.printStackTrace();
                return new byte[0];
            }
        }
        return null;
    }
    @Operation(summary = "Api for open_general", description = "this api used to open all files")
    @GetMapping(value = "/open_general/{fileName}", produces = MediaType.ALL_VALUE)
    public byte[] open_general(@PathVariable("fileName") String fileName) {
        log.info("get attach {}",fileName);
        return attachService.open_general(fileName);
    }
    @Operation(summary = "Api for open_general_pagination", description = "this api used to open all files pagination")
    @GetMapping("open_general/pagination")
    public ResponseEntity<PageImpl<?>> open_general_pagination(@RequestParam Integer page, @RequestParam Integer size) {
        log.info("get attach pagination {}",size);
        return ResponseEntity.ok(attachService.getPagination(page, size));
    }
    @Operation(summary = "Api for delete", description = "this api is for opening all files by uuids")
    @DeleteMapping("/delete/{uuid}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> delete(@PathVariable("uuid") String uuid) {
        log.info("delete attach by uuid {}",uuid);
        return ResponseEntity.ok(attachService.delete(uuid));
    }

    @Operation(summary = "Api for download", description = "this api will download all file by name")
    @GetMapping("/download/{fineName}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Resource> download(@PathVariable("fineName") String fileName) {
        log.info("download attach {}",fileName);
        return attachService.download(fileName);
    }

}
