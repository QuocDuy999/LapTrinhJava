package com.example.genderhealthcare.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.genderhealthcare.entity.BlogPost;

public interface BlogPostRepository extends JpaRepository<BlogPost, Long> {
}
