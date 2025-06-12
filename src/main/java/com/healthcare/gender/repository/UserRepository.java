package com.example.genderhealthcare.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.genderhealthcare.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email); // ✅ Trả về Optional<User>
    Optional<User> findByUsername(String username); // ✅ Tìm kiếm theo username
}
