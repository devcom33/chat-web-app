package com.example.demo;

import com.example.demo.entities.AppRole;
import com.example.demo.entities.AppUser;
import com.example.demo.service.AccountService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;



@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {

        SpringApplication.run(DemoApplication.class, args);
    }

/*

    @Bean
    CommandLineRunner start(AccountService accountService){
        return args -> {
            accountService.addNewRole(new AppRole(null, "USER"));
            accountService.addNewRole(new AppRole(null, "ADMIN"));



            accountService.addNewUser(new AppUser(null,"user1", "user1@gmail.com","+212 787827822","1234",new ArrayList<>()));
            //accountService.addNewUser(new AppUser(null,"admin","1234",new ArrayList<>()));
            accountService.addNewUser(new AppUser(null,"user2", "user2@gmail.com", "+212 787827822","1234",new ArrayList<>()));
            accountService.addNewUser(new AppUser(null,"user3", "user3@gmail.com","+212 787827822","1234",new ArrayList<>()));
            accountService.addNewUser(new AppUser(null,"user4", "user4@gmail.com","+212 787827822","1234",new ArrayList<>()));


            accountService.addRoleToUser("user1", "USER");
            accountService.addRoleToUser("user2", "USER");
            accountService.addRoleToUser("user3", "USER");
            accountService.addRoleToUser("user4", "USER");
            //accountService.addRoleToUser("admin", "ADMIN");

        };

    }

 */
    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
        //return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
