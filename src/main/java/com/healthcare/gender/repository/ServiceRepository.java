package com.healthcare.gender.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.healthcare.gender.model.entity.Service;

public interface ServiceRepository extends JpaRepository<Service, Long> {
}
