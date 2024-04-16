package com.example.foodx_be.controller;

import com.example.foodx_be.enity.ReviewImage;
import com.example.foodx_be.service.CloudiaryService;
import com.example.foodx_be.service.RestaurantReviewImageService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/cloudinary")
@AllArgsConstructor
public class CloudiaryController {
    CloudiaryService cloudiaryService;
    RestaurantReviewImageService restaurantReviewImageService;

    @GetMapping("/list")
    public ResponseEntity<List<ReviewImage>> list() {
        List<ReviewImage> list = restaurantReviewImageService.reviewImageList();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @PostMapping("/upload")
    @ResponseBody
    public ResponseEntity<String> upload(@RequestParam MultipartFile multipartFile,
                                         @RequestParam UUID idReview) throws IOException {
        BufferedImage bi = ImageIO.read(multipartFile.getInputStream());
        if (bi == null) {
            return new ResponseEntity<>("Image non valide!", HttpStatus.BAD_REQUEST);
        }
        Map result = cloudiaryService.upload(multipartFile);
        ReviewImage image = new ReviewImage();
        image.setImageId((String) result.get("public_id"));
        image.setName((String) result.get("original_filename"));
        image.setImageUrl((String) result.get("url"));
        restaurantReviewImageService.save(idReview,image);
        return new ResponseEntity<>("image ajoutée avec succès ! ", HttpStatus.OK);
    }
}
