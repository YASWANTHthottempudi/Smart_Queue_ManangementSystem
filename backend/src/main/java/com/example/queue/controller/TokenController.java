package com.example.queue.controller;

import com.example.queue.dto.TokenRequest;
import com.example.queue.dto.TokenResponse;
import com.example.queue.service.TokenService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tokens")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class TokenController {

    private final TokenService tokenService;

    @PostMapping
    public ResponseEntity<TokenResponse> createToken(
            @Valid @RequestBody TokenRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        TokenResponse response = tokenService.createToken(request, userDetails.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{tokenId}")
    public ResponseEntity<TokenResponse> getTokenStatus(
            @PathVariable Long tokenId,
            @AuthenticationPrincipal UserDetails userDetails) {
        TokenResponse response = tokenService.getTokenStatus(tokenId, userDetails.getUsername());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/my")
    public ResponseEntity<List<TokenResponse>> getMyTokens(
            @AuthenticationPrincipal UserDetails userDetails) {
        List<TokenResponse> tokens = tokenService.getUserTokens(userDetails.getUsername());
        return ResponseEntity.ok(tokens);
    }
}
