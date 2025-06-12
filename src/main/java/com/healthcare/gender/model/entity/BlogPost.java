package com.example.genderhealthcare.entity; 

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "blog_posts")
public class BlogPost {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String title;
    
    @Column(length = 5000)
    private String content;
    
    private LocalDateTime postedDate;
    
    // Có thể liên kết với tác giả (User)
    @ManyToOne
    @JoinColumn(name = "author_id")
    private User author;

    // Getters và Setters
    public Long getId() {
        return id;
    }    

    public void setId(Long id) {
        this.id = id;
    }    

    public String getTitle() {
        return title;
    }    

    public void setTitle(String title) {
        this.title = title;
    }    

    public String getContent() {
        return content;
    }    

    public void setContent(String content) {
        this.content = content;
    }    

    public LocalDateTime getPostedDate() {
        return postedDate;
    }    

    public void setPostedDate(LocalDateTime postedDate) {
        this.postedDate = postedDate;
    }    

    public User getAuthor() {
        return author;
    }    

    public void setAuthor(User author) {
        this.author = author;
    }
}
