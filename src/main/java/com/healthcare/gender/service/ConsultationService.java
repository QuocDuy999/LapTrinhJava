package com.healthcare.gender.service;

import com.healthcare.gender.model.entity.Consultation;
import com.healthcare.gender.repository.ConsultationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ConsultationService {

    @Autowired
    private ConsultationRepository consultationRepository;

    public Consultation save(Consultation consultation) {
        return consultationRepository.save(consultation);
    }

    public List<Consultation> findByUsername(String username) {
        return consultationRepository.findByUsername(username);
    }

    public List<Consultation> findAll() {
        return consultationRepository.findAll();
    }

    public Optional<Consultation> findById(Long id) {
        return consultationRepository.findById(id);
    }

    public void deleteById(Long id) {
        consultationRepository.deleteById(id);
    }

    public Consultation answer(Long id, String answer) {
        Optional<Consultation> optional = consultationRepository.findById(id);
        if (optional.isPresent()) {
            Consultation consultation = optional.get();
            consultation.setAnswer(answer);
            consultation.setAnsweredAt(LocalDateTime.now());
            return consultationRepository.save(consultation);
        }
        return null;
    }

    public boolean deleteByIdAndUsername(Long id, String username) {
        Consultation consultation = consultationRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Không tìm thấy câu hỏi"));

        if (!consultation.getUsername().equals(username)) {
            throw new RuntimeException("Bạn không có quyền xoá câu hỏi này");
        }

        if (consultation.getAnswer() != null) {
            throw new RuntimeException("Câu hỏi đã được trả lời, không thể thu hồi");
        }

        consultationRepository.deleteById(id);
        return true;
    }
}
