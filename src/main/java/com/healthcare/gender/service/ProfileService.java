package com.healthcare.gender.service;

import com.healthcare.gender.model.entity.Profile;
import com.healthcare.gender.repository.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProfileService {

    @Autowired
    private ProfileRepository repository;

    public Optional<Profile> getProfileByUsername(String username) {
        return repository.findByUsername(username);
    }

    public Profile saveOrUpdate(Profile profile) {
        Optional<Profile> existing = repository.findByUsername(profile.getUsername());
        if (existing.isPresent()) {
            Profile p = existing.get();
            p.setName(profile.getName());
            p.setDob(profile.getDob());
            p.setGender(profile.getGender());
            p.setPhone(profile.getPhone());
            p.setAddress(profile.getAddress());
            p.setHeight(profile.getHeight());             // ✅ chiều cao
            p.setWeight(profile.getWeight());             // ✅ cân nặng
            p.setMedicalHistory(profile.getMedicalHistory()); // ✅ tiểu sử bệnh
            return repository.save(p);
        } else {
            return repository.save(profile);
        }
    }
}
