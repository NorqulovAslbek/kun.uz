package com.example.controller;

import com.example.dto.RegionDTO;
import com.example.service.RegionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/region")
public class RegionController {
    @Autowired
    private RegionService regionService;

    @PostMapping("")
    public ResponseEntity<RegionDTO> create(@RequestBody RegionDTO dto) {
        return ResponseEntity.ok(regionService.create(dto));
    }

    @PutMapping("/update")
    public ResponseEntity<?> update(@RequestParam("id") Integer id, @RequestBody RegionDTO dto) {
        return ResponseEntity.ok(regionService.update(id, dto));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> delete(@RequestParam Integer id) {
        return ResponseEntity.ok(regionService.delete(id));
    }

    @GetMapping("")
    public ResponseEntity<List<RegionDTO>> getAll() {
        return ResponseEntity.ok(regionService.getAll());
    }
    @GetMapping("/lang")
    public ResponseEntity<List<RegionDTO>> getByLang(@RequestParam String lang){
        return ResponseEntity.ok(regionService.getByLang(lang));
    }


}
