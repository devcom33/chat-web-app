package com.example.demo.service;

import com.example.demo.entities.AppRole;
import com.example.demo.entities.AppUser;

import com.example.demo.repo.AppRoleRepository;
import com.example.demo.repo.AppUserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class AccountServiceImpl implements AccountService {

    private AppUserRepository appUserRepository;
    private AppRoleRepository appRoleRepository;
    private PasswordEncoder passwordEncoder;

    public AccountServiceImpl(AppUserRepository appUserRepository, AppRoleRepository appRoleRepository, PasswordEncoder passwordEncoder) {
        this.appUserRepository = appUserRepository;
        this.appRoleRepository = appRoleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public AppUser addNewUser(AppUser appUser) {

        String pw=appUser.getPassword();
        appUser.setPassword(passwordEncoder.encode(pw));
        return appUserRepository.save(appUser);
    }
    @Override
    public AppUser updateUser(AppUser appUser) {
        AppUser existingUser = appUserRepository.findById(appUser.getId())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        if (appUser.getEmail() != null) {
            existingUser.setEmail(appUser.getEmail());
        }
        if (appUser.getPhone() != null) {
            existingUser.setEmail(appUser.getEmail());
        }

        return appUserRepository.save(existingUser);
    }
    @Override
    public AppUser getUserById(Long id) {
        return appUserRepository.findById(id).orElse(null);
    }

    @Override
    public AppRole addNewRole(AppRole appRole) {
        return appRoleRepository.save(appRole);
    }

    @Override
    public void addRoleToUser(String username, String roleName) {
        AppUser appUser = appUserRepository.findByUsername(username);
        AppRole appRole = appRoleRepository.findByRoleName(roleName);
        appUser.getAppRoles().add(appRole);
    }

    @Override
    public AppUser loadUserByUsername(String username) {
        System.out.println("Searching for user: " + appUserRepository.findByUsername(username));
        return appUserRepository.findByUsername(username);
    }
    @Override
    public AppUser loadUserById(Long id){
        return appUserRepository.findById(id).orElse(null);
    }

    @Override
    public List<AppUser> listUsers() {
        return appUserRepository.findAll();
    }

    @Override
    public void updatePassword(String username, String oldPassword, String newPassword) {
        AppUser user = appUserRepository.findByUsername(username);
        if (user == null) {
            throw new RuntimeException("User not found");
        }

        // Check if the old password matches
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new RuntimeException("Old password is incorrect");
        }

        // Update the password
        user.setPassword(passwordEncoder.encode(newPassword));
        appUserRepository.save(user);
    }


}
