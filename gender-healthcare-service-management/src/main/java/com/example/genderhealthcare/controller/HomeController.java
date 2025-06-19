package com.example.genderhealthcare.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.genderhealthcare.entity.Service;
import com.example.genderhealthcare.repository.ServiceRepository;

@Controller
public class HomeController {

    @Autowired
    private ServiceRepository serviceRepository;

    @GetMapping("/")
    public String home(Model model) {
        List<Service> services = serviceRepository.findAll();
        model.addAttribute("services", services);
        return "home";  // Trả về file home.html
    }
}
