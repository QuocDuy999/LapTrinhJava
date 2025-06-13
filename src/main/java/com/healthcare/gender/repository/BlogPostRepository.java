package com.healthcare.gender.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.healthcare.gender.model.entity.BlogPost;

public interface BlogPostRepository extends JpaRepository<BlogPost, Long> {
}
