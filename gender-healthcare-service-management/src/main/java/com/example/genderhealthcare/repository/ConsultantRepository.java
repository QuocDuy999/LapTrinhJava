package com.example.genderhealthcare.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.genderhealthcare.entity.Consultant;

public interface ConsultantRepository extends JpaRepository<Consultant, Long> {
}