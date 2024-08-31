package com.example.demo.service;

import com.example.demo.entities.AppRole;
import com.example.demo.entities.AppUser;

import java.util.List;

public interface AccountService {
    AppUser addNewUser(AppUser appUser);
    AppUser updateUser(AppUser appUser);
    AppUser getUserById(Long id);
    AppRole addNewRole(AppRole appRole);
    void addRoleToUser(String username, String roleName);
    AppUser loadUserByUsername(String username);
    AppUser loadUserById(Long id);
    List<AppUser> listUsers();

    void updatePassword(String username, String oldPassword, String newPassword);


}
