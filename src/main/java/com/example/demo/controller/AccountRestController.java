package com.example.demo.controller;

import com.example.demo.sec.util.JwtUtil;
import com.example.demo.entities.AppRole;
import com.example.demo.entities.AppUser;
import com.example.demo.service.AccountService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.demo.DTO.PasswordUpdateRequest;

import java.util.List;

@RestController
public class AccountRestController {
    private AccountService accountService;
    @Autowired
    private JwtUtil jwtUtil;


    public AccountRestController(AccountService accountService) {
        this.accountService = accountService;
    }
    @GetMapping(path = "/users")
    public List<AppUser> appUsers(){
        return accountService.listUsers();
    }

    @PostMapping(path="/users")
    public AppUser saveUser(@RequestBody AppUser appUser){
        return accountService.addNewUser(appUser);
    }

    @GetMapping(path = "/user/profile")
    public ResponseEntity<AppUser> profile(HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        String username = jwtUtil.getUsernameFromToken(token);
        AppUser user = accountService.loadUserByUsername(username);
        return ResponseEntity.ok(user);
    }
    @GetMapping(path = "/user/profile/{id}")
    public ResponseEntity<AppUser> profile(@PathVariable Long id){
        AppUser user = accountService.loadUserById(id);
        return ResponseEntity.ok(user);
    }
    @GetMapping(path = "/user/{username}")
    public ResponseEntity<AppUser> profile(@PathVariable String username){
        AppUser user = accountService.loadUserByUsername(username);
        return ResponseEntity.ok(user);
    }
    @PutMapping(path = "/profile/edit")
    public ResponseEntity<AppUser> updateUser(@RequestBody AppUser appUser){
        AppUser updatedUser = accountService.updateUser(appUser);

        if (updatedUser != null) {
            return ResponseEntity.ok(updatedUser);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
    @PostMapping("/{username}/password")
    public void updatePassword(@PathVariable String username,
                               @RequestBody PasswordUpdateRequest request) {
        accountService.updatePassword(username, request.getOldPassword(), request.getNewPassword());
    }

    @PostMapping(path="/roles")
    public AppRole saveRole(@RequestBody AppRole appRole){
        return accountService.addNewRole(appRole);
    }

    @PostMapping(path="/addRoleToUser")
    public void addRoleToUser(@RequestBody RoleUserForm roleUserForm) {
        accountService.addRoleToUser(roleUserForm.getUsername(), roleUserForm.getRoleName());
    }
}
@Data
class RoleUserForm{
    private String username;
    private String roleName;
}

