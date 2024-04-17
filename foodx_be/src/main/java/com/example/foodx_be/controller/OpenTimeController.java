package com.example.foodx_be.controller;

import com.example.foodx_be.dto.OpenTimeDTO;
import com.example.foodx_be.enity.OpenTime;
import com.example.foodx_be.service.OpenTimeService;
import lombok.AllArgsConstructor;
import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/opentime")
@AllArgsConstructor
public class OpenTimeController {
    private OpenTimeService openTimeService;

    @GetMapping("/{idRestaurant}")
    public ResponseEntity<List<OpenTimeDTO>> getOpenTimeOfRestaurant(@PathVariable UUID idRestaurant){
        return new ResponseEntity<>(openTimeService.getOpenTimeOfRestaurant(idRestaurant) ,HttpStatus.OK);
    }


}
