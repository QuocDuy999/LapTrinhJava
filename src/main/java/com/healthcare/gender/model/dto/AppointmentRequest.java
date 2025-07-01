package com.healthcare.gender.model.dto;

public class AppointmentRequest {
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
