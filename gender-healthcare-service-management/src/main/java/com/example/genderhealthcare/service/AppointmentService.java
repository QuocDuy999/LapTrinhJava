package com.example.genderhealthcare.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.genderhealthcare.entity.Appointment;
import com.example.genderhealthcare.repository.AppointmentRepository;

@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    public void saveAppointment(Appointment appointment) {
        appointmentRepository.save(appointment);
    }

    public List<Appointment> findByUserId(Long userId) {
        return appointmentRepository.findByUserId(userId);
    }
}
