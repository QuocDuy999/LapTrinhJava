package com.example.genderhealthcare.controller;

import com.example.genderhealthcare.entity.Appointment;
import com.example.genderhealthcare.entity.User;
import com.example.genderhealthcare.service.AppointmentService;
import com.example.genderhealthcare.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/appointment")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private UserService userService;

    @PostMapping("/book")
    public String bookAppointment(@AuthenticationPrincipal UserDetails userDetails,
                                  @RequestParam LocalDateTime appointmentDate) {
        String email = userDetails.getUsername();
        Optional<User> optionalUser = userService.findByEmail(email);

        if (optionalUser.isEmpty()) {
            return "Không tìm thấy người dùng!";
        }

        Appointment appointment = new Appointment();
        appointment.setUser(optionalUser.get());
        appointment.setAppointmentDate(appointmentDate);

        appointmentService.saveAppointment(appointment);
        return "redirect:/customer";
    }

    @GetMapping("/list")
    public List<Appointment> listAppointments(@AuthenticationPrincipal UserDetails userDetails) {
        String email = userDetails.getUsername();
        Optional<User> optionalUser = userService.findByEmail(email);

        if (optionalUser.isEmpty()) {
            throw new RuntimeException("Không tìm thấy người dùng!");
        }

        return appointmentService.findByUserId(optionalUser.get().getId());
    }
}
