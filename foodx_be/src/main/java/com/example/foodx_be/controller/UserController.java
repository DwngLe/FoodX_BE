package com.example.foodx_be.controller;

import com.example.foodx_be.dto.AddBusinessProofCommand;
import com.example.foodx_be.dto.UpdateUserComand;
import com.example.foodx_be.dto.UserDTO;
import com.example.foodx_be.service.BusinessProofService;
import com.example.foodx_be.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@AllArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {
    private UserService userService;
    private BusinessProofService businessProofService;

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> findById(@PathVariable UUID id) {
        return new ResponseEntity<>(userService.getUserByID(id), HttpStatus.OK);
    }

    @PostMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@RequestBody UpdateUserComand updateUserComand) {
        return new ResponseEntity<>(userService.updateUser(updateUserComand), HttpStatus.OK);
    }


    @GetMapping("/search")
    public ResponseEntity<Page<UserDTO>> findUsersByName(@RequestParam(name = "name") String name,
                                                         @RequestParam(name = "pageNo", defaultValue = "0") int pageNo,
                                                         @RequestParam(name = "limit", defaultValue = "5") int limit) {
        return new ResponseEntity<>(userService.getUsersByName(pageNo, limit, name), HttpStatus.OK);
    }

    @PostMapping("/avatar{username}")
    public ResponseEntity<HttpStatus> updateUserAvatar(@RequestParam MultipartFile multipartFile,
                                                       @PathVariable String username) throws IOException {
        userService.updateUserAvatar(username, multipartFile);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/claimBusiness/{idRestaurant}")
    public ResponseEntity<HttpStatus> addBusinessProof(@PathVariable UUID idRestaurant,
                                                       @RequestPart("data") AddBusinessProofCommand addBusinessProofCommand,
                                                       @RequestPart MultipartFile multipartFile) throws IOException{
        addBusinessProofCommand.setIdRestaurant(idRestaurant);
        businessProofService.addBusinessProof(addBusinessProofCommand, multipartFile);
        return  new ResponseEntity<>(HttpStatus.OK);
    }
}
