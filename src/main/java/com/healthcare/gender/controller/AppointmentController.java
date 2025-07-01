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
}

class AppointmentRequest {
    private String department;
    private String doctorName;
    private String illness;
    private String date;
    private String time;

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public String getDoctorName() { return doctorName; }
    public void setDoctorName(String doctorName) { this.doctorName = doctorName; }

    public String getIllness() { return illness; }
    public void setIllness(String illness) { this.illness = illness; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }
}
