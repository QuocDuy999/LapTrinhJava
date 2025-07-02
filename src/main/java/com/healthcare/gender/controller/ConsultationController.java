package com.healthcare.gender.controller;

import com.healthcare.gender.model.entity.Consultation;
import com.healthcare.gender.service.ConsultationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/questions")
public class ConsultationController {

    @Autowired
    private ConsultationService consultationService;

    // ✅ 1. Người dùng gửi câu hỏi
    @PostMapping("/ask")
    public Consultation ask(@RequestBody Consultation consultation,
                            @AuthenticationPrincipal UserDetails userDetails) {
        consultation.setUsername(userDetails.getUsername());
        consultation.setAskedAt(LocalDateTime.now());
        return consultationService.save(consultation);
    }

    // ✅ 2. Người dùng xem lịch sử câu hỏi của họ
    @GetMapping("/my-questions")
    public List<Consultation> myQuestions(@AuthenticationPrincipal UserDetails userDetails) {
        return consultationService.findByUsername(userDetails.getUsername());
    }

    // ✅ 3. Admin + Tư vấn viên: xem tất cả câu hỏi
    @PreAuthorize("hasAuthority('ROLE_CONSULTANT') or hasAuthority('ROLE_ADMIN')")
    @GetMapping("/all")
    public List<Consultation> getAllConsultations() {
        return consultationService.findAll();
    }

    // ✅ 4. Admin hoặc tư vấn viên: trả lời câu hỏi
    @PreAuthorize("hasAuthority('ROLE_CONSULTANT') or hasAuthority('ROLE_ADMIN')")
    @PostMapping("/answer/{id}")
    public Consultation answer(@PathVariable Long id, @RequestBody String answer) {
        return consultationService.answer(id, answer);
    }

    // ✅ 5. Người dùng: tự xoá câu hỏi của mình
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteQuestion(@PathVariable Long id,
                                            @AuthenticationPrincipal UserDetails userDetails) {
        try {
            boolean deleted = consultationService.deleteByIdAndUsername(id, userDetails.getUsername());
            if (deleted) {
                return ResponseEntity.ok().build();
            }
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    // ✅ 6. Admin: xoá các câu hỏi đã được trả lời
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @DeleteMapping("/admin/delete/{id}")
    public ResponseEntity<String> deleteAnsweredByAdmin(@PathVariable Long id) {
        Optional<Consultation> consultationOpt = consultationService.findById(id);
        if (consultationOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy câu hỏi.");
        }

        Consultation c = consultationOpt.get();
        if (c.getAnswer() == null || c.getAnswer().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Câu hỏi chưa được trả lời, không thể xoá.");
        }

        consultationService.deleteById(id);
        return ResponseEntity.ok("Đã xoá câu hỏi đã trả lời.");
    }
}
