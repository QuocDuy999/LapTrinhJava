package com.healthcare.gender.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.healthcare.gender.model.entity.User;
import com.healthcare.gender.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public boolean existsByEmail(String email) {
        if (email == null) {
            return false;
        }
        Optional<User> optionalUser = userRepository.findByEmail(email);
        return optionalUser.isPresent();
    }
    

    public boolean existsByUsername(String username) {
        return username != null && userRepository.findByUsername(username).isPresent();
    }
    

    public void saveUser(User user) {
        if (user.getEmail() == null || user.getPassword() == null || user.getName() == null) {
            throw new IllegalArgumentException("Tên, Email và Mật khẩu không được để trống!");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword())); // Mã hóa mật khẩu
        userRepository.save(user);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public boolean validateUser(String email, String rawPassword) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        return optionalUser.isPresent() && passwordEncoder.matches(rawPassword, optionalUser.get().getPassword());
    }
}
