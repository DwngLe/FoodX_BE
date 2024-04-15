package com.example.foodx_be.controller;

import com.example.foodx_be.dto.UpdateUserComand;
import com.example.foodx_be.dto.UserDTO;
import com.example.foodx_be.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@AllArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController {
    UserService userService;

    @GetMapping("/profile/{id}")
    public ResponseEntity<UserDTO> findById(@PathVariable UUID id) {
        return new ResponseEntity<>(userService.getUserByID(id), HttpStatus.OK);
    }

    @PostMapping("/profile/update")
    public ResponseEntity<UserDTO> updateUser(@RequestBody UpdateUserComand updateUserComand) {
        return new ResponseEntity<>(userService.updateUser(updateUserComand), HttpStatus.OK);
    }


    @GetMapping("/search")
    public ResponseEntity<Page<UserDTO>> findUsersByName(@RequestParam(name = "name") String name,
                                                         @RequestParam(name = "pageNo", defaultValue = "0") int pageNo,
                                                         @RequestParam(name = "limit", defaultValue = "5") int limit) {
        return new ResponseEntity<>(userService.getUsersByName(pageNo, limit, name), HttpStatus.OK);
    }
}
