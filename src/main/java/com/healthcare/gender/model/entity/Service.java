package com.healthcare.gender.model.entity; // Package phải đặt ở dòng đầu tiên

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "services")
public class Service {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;
    
    @Column(length = 1000)
    private String description;
    
    private String iconClass; // FontAwesome icon

    // Getters và Setters
    public Long getId() {
        return id;
    }    

    public void setId(Long id) {
        this.id = id;
    }    

    public String getName() {
        return name;
    }    

    public void setName(String name) {
        this.name = name;
    }    

    public String getDescription() {
        return description;
    }    

    public void setDescription(String description) {
        this.description = description;
    }    

    public String getIconClass() {
        return iconClass;
    }    

    public void setIconClass(String iconClass) {
        this.iconClass = iconClass;
    }
}
