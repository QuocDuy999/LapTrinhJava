package com.healthcare.gender.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.healthcare.gender.model.entity.BlogPost;
import com.healthcare.gender.model.entity.Service;
import com.healthcare.gender.repository.BlogPostRepository;
import com.healthcare.gender.repository.ServiceRepository;

@Controller
public class HomeController {

    @Autowired
    private BlogPostRepository blogPostRepository;

    @Autowired
    private ServiceRepository serviceRepository;

    @GetMapping("/home")
    public String home(Model model) {
        List<BlogPost> blogs = blogPostRepository.findAll();
        List<Service> services = serviceRepository.findAll();

        model.addAttribute("blogs", blogs);
        model.addAttribute("services", services);

        return "home";  // Trả về file home.html
    }
}
