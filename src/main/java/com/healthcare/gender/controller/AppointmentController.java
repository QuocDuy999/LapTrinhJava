package com.healthcare.gender.controller;

import com.healthcare.gender.model.entity.Appointment;
import com.healthcare.gender.model.entity.User;
import com.healthcare.gender.repository.AppointmentRepository;
import com.healthcare.gender.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.healthcare.gender.model.dto.AppointmentRequest;
@RestController
@RequestMapping("/api/appointment")
@CrossOrigin
public class AppointmentController {

    @Autowired
    private AppointmentRepository appointmentRepo;

    @Autowired
    private UserRepository userRepo;

    @PostMapping
    public Appointment book(@RequestBody AppointmentRequest request, Authentication authentication) {
        String username = authentication.getName();
        User user = userRepo.findByUsername(username).orElseThrow();

        Appointment app = new Appointment();
        app.setUser(user);
        app.setDepartment(request.getDepartment());
        app.setDoctorName(request.getDoctorName());
        app.setIllness(request.getIllness());
        app.setAppointmentDate(LocalDateTime.of(
                LocalDate.parse(request.getDate()),
                LocalTime.parse(request.getTime())
        ));
        app.setStatus("Đã đặt");

        return appointmentRepo.save(app);
    }

    @GetMapping("/history")
    public List<Appointment> history(Authentication authentication) {
        String username = authentication.getName();
        return appointmentRepo.findByUserUsername(username);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> cancelAppointment(@PathVariable Long id, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Chưa xác thực người dùng");
        }

        String username = authentication.getName();
        Appointment appointment = appointmentRepo.findById(id).orElse(null);
        if (appointment == null) {
            return ResponseEntity.notFound().build();
        }

        if (!appointment.getUser().getUsername().equals(username)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Không có quyền hủy lịch này");
        }

        appointmentRepo.delete(appointment);
        return ResponseEntity.ok("Đã hủy lịch hẹn");
    }
}
