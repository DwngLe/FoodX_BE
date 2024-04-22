package com.example.foodx_be.controller;

import com.example.foodx_be.dto.*;
import com.example.foodx_be.enity.Restaurant;
import com.example.foodx_be.enity.Tag;
import com.example.foodx_be.service.*;
import com.example.foodx_be.ulti.RestaurantState;
import com.example.foodx_be.ulti.UpdateState;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/admin")
public class AdminController {
    private RestaurantService restaurantService;
    private UserService userService;
    private AdminService adminService;
    private BusinessProofService businessProofService;
    private TagService tagService;

    @GetMapping("/restaurants/view/{restaurantName}")
    public ResponseEntity<Restaurant> getRestaurantByRestaurantState(@PathVariable String restaurantName) {
        return new ResponseEntity<>(restaurantService.getRestaurantEnityByName(restaurantName), HttpStatus.OK);
    }

    @GetMapping("/reviewUpdates")
    public ResponseEntity<Page<RestaurantUpdateDTO>> getUpdateRestaurantList(@RequestParam UpdateState updateState,
                                                                             @RequestParam int pageNo,
                                                                             @RequestParam int limit) {
        return new ResponseEntity<>(adminService.getRestaurantUpdateList(pageNo, limit, updateState), HttpStatus.OK);
    }

    @PostMapping("/reviewUpdates/{idUpdate}")
    public ResponseEntity<HttpStatus> reviewRestaurantUpdate(@PathVariable UUID idUpdate,
                                                             @RequestBody ReviewUpdate reviewUpdate) {
        adminService.reviewRestaurantUpdate(idUpdate, reviewUpdate);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/restaurants/{idRestaurant}/updateState")
    public ResponseEntity<HttpStatus> reviewRestaurantUpdate(@PathVariable UUID idRestaurant,
                                                             @RequestBody ReviewRestaurantState restaurantState) {
        adminService.reviewRestaurantState(idRestaurant, restaurantState);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @GetMapping("/restaurants/view")
    public  ResponseEntity<Page<RestaurantDTO>> getRestaurantByRestaurantState(@RequestParam RestaurantState restaurantState,
                                                                               @RequestParam int pageNo,
                                                                               @RequestParam int limit){
        return new ResponseEntity<>(restaurantService.getRestaurantByRestaurantState(pageNo, limit, restaurantState), HttpStatus.OK);
    }

    @GetMapping("/businessProof")
    public ResponseEntity<Page<BusinessProofDTO>> getListBusinessProofByState(@RequestParam UpdateState state,
                                                                              @RequestParam int pageNo,
                                                                              @RequestParam int limit){
        return new ResponseEntity<>(businessProofService.getListBusinessProofByState(pageNo, limit, state), HttpStatus.OK);
    }

    @GetMapping("/businessProof/{idBusinessProof}")
    public ResponseEntity<BusinessProofDTO> getBusinessProof(@PathVariable UUID idBusinessProof){
        return new ResponseEntity<>(businessProofService.getBusinessProof(idBusinessProof), HttpStatus.OK);
    }

    @PostMapping("/businessProof/{idBusinessProof}")
    public ResponseEntity<HttpStatus> reviewBusinessProof(@PathVariable UUID idBusinessProof,
                                                          @RequestParam UpdateState updateState){
        businessProofService.reviewBusinessProof(idBusinessProof, updateState);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/tag/add")
    public ResponseEntity<HttpStatus> addTag(@RequestBody List<TagDTO> tagDTOList){
        tagService.addTag(tagDTOList);
        return new ResponseEntity<>(HttpStatus.OK);
    }


}
