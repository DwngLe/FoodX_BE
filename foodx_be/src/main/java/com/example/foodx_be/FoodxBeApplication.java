package com.example.foodx_be;

import com.example.foodx_be.enity.User;
import com.example.foodx_be.enums.AccountState;
import com.example.foodx_be.enums.Role;
import com.example.foodx_be.repository.UserRepository;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.HashSet;

@SpringBootApplication
public class FoodxBeApplication {

    public static void main(String[] args) {
        SpringApplication.run(FoodxBeApplication.class, args);

    }

    @Bean
    ApplicationRunner applicationRunner(UserRepository userRepository) {
        return args -> {
            if (userRepository.findByUsername("admin").isEmpty()) {
                var roles = new HashSet<String>();
                roles.add(Role.ADMIN.name());

                User user = User.builder()
                        .username("admin")
                        .password(bCryptPasswordEncoder().encode("admin"))
                        .roles(roles)
                        .accountState(AccountState.ACTIVE)
                        .city("abc")
                        .ward("abc")
                        .district("abc")
                        .email("abc")
                        .name("abc")
                        .phoneNumber("123")
                        .points(0)
                        .build();
                userRepository.save(user);
            }
        };
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }


}
