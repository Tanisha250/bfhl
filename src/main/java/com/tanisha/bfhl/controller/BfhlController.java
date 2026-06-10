package com.tanisha.bfhl.controller;

import com.tanisha.bfhl.dto.BfhlRequestDto;
import com.tanisha.bfhl.dto.BfhlResponseDto;
import com.tanisha.bfhl.service.BfhlService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/bfhl")
public class BfhlController {

    private final BfhlService bfhlService;

    public BfhlController(BfhlService bfhlService) {
        this.bfhlService = bfhlService;
    }

    /**
     * POST /bfhl
     * Accepts a JSON array and returns processed categorized results.
     */
    @PostMapping
    public ResponseEntity<BfhlResponseDto> processData(@Valid @RequestBody BfhlRequestDto request) {
        BfhlResponseDto response = bfhlService.processData(request);
        return ResponseEntity.ok(response);

    }
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {

        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("statusCode", HttpStatus.OK.value());

        return ResponseEntity.ok(response);
    }
}
