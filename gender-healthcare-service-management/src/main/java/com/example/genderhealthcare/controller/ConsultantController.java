package com.example.genderhealthcare.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.genderhealthcare.entity.Consultant;
import com.example.genderhealthcare.service.ConsultantService;

@RestController
@RequestMapping("/api/consultant")
public class ConsultantController {

    @Autowired
    private ConsultantService consultantService;

    @GetMapping("/list")
    public List<Consultant> listConsultants() {
        return consultantService.findAllConsultants();
    }
}