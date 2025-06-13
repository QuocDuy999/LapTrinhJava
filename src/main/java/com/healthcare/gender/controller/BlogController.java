package com.healthcare.gender.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.healthcare.gender.model.entity.BlogPost;
import com.healthcare.gender.repository.BlogPostRepository;

@RestController
@RequestMapping("/blogs")
public class BlogController {
    
    @Autowired
    private BlogPostRepository blogPostRepository;
    
    @GetMapping
    public List<BlogPost> getAllBlogs() {
        return blogPostRepository.findAll();
    }
    
    // Endpoint tạo bài blog (chỉ có thể truy cập với quyền phù hợp, ví dụ: CONSULTANT, STAFF, MANAGER, ADMIN)
    @PostMapping
    public BlogPost createBlog(@RequestBody BlogPost blogPost) {
        blogPost.setPostedDate(LocalDateTime.now());
        return blogPostRepository.save(blogPost);
    }
}
