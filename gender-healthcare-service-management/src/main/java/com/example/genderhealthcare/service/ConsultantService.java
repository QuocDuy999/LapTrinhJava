package com.example.genderhealthcare.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.genderhealthcare.entity.Consultant;
import com.example.genderhealthcare.repository.ConsultantRepository;

@Service
public class ConsultantService {
    @Autowired
    private ConsultantRepository consultantRepository;

    public List<Consultant> findAllConsultants() {
        List<Consultant> consultants = consultantRepository.findAll();
        System.out.println("Danh sách tư vấn viên: " + consultants);
        return consultants;
    }

    public Consultant findById(Long id) {
        return consultantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy tư vấn viên!"));
    }
}

