package com.example.genderhealthcare.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.genderhealthcare.entity.Service;

public interface ServiceRepository extends JpaRepository<Service, Long> {
}
