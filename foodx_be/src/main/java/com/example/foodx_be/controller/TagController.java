package com.example.foodx_be.controller;

import com.example.foodx_be.dto.TagDTO;
import com.example.foodx_be.service.TagService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/tag")
public class TagController {
    private TagService tagService;

    @GetMapping("/viewAll")
    public ResponseEntity<List<TagDTO>> getListTag(){
        return new ResponseEntity<>(tagService.getListTag(), HttpStatus.OK);
    }
}
