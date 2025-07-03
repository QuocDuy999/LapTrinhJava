package com.healthcare.gender.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "profiles")
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false,columnDefinition = "NVARCHAR(255)")
    private String name;

    private String dob;
    @Column(columnDefinition = "NVARCHAR(255)")
    private String gender;


    private String phone;
    @Column(nullable = false,columnDefinition = "NVARCHAR(255)")
    private String address;

    private Double height; // Chiều cao (cm)
    private Double weight; // Cân nặng (kg)

    @Column(length = 1000)
    private String medicalHistory; // Tiểu sử bệnh

    @Column(unique = true)
    private String username; // Liên kết với người dùng
}
