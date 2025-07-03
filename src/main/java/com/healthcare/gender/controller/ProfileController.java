package com.healthcare.gender.controller;

import com.healthcare.gender.model.entity.Profile;
import com.healthcare.gender.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {

    @Autowired
    private ProfileService service;

    // Lấy hồ sơ cá nhân theo người dùng hiện tại (từ JWT)
    @GetMapping("/my")
    public ResponseEntity<?> getMyProfile(Authentication authentication) {
        String username = authentication.getName();
        Optional<Profile> profile = service.getProfileByUsername(username);
        return profile.map(ResponseEntity::ok)
                      .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Thêm hoặc cập nhật hồ sơ cá nhân
    @PostMapping
    public ResponseEntity<?> saveOrUpdateProfile(@RequestBody Profile profile, Authentication authentication) {
        profile.setUsername(authentication.getName());
        Profile saved = service.saveOrUpdate(profile);
        return ResponseEntity.ok(saved);
    }
}
