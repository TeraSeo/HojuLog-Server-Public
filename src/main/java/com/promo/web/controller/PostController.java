package com.promo.web.controller;

import com.promo.web.dto.*;
import com.promo.web.service.*;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("api/post")
@Slf4j
public class PostController {

    private final TechnologyPostService technologyPostService;
    private final RestaurantPostService restaurantPostService;
    private final LifeStylePostService lifeStylePostService;
    private final EducationPostService educationPostService;
    private final EntertainmentPostService entertainmentPostService;

    @Autowired
    public PostController(TechnologyPostService technologyPostService, RestaurantPostService restaurantPostService, LifeStylePostService lifeStylePostService, EducationPostService educationPostService, EntertainmentPostService entertainmentPostService) {
        this.technologyPostService = technologyPostService;
        this.restaurantPostService = restaurantPostService;
        this.lifeStylePostService = lifeStylePostService;
        this.educationPostService = educationPostService;
        this.entertainmentPostService = entertainmentPostService;
    }

    @PostMapping("create/technology")
    public ResponseEntity<Boolean> createTechnologyPost(
            @Valid @RequestPart TechnologyPostDto technologyPostDto,
            @RequestPart(required = false) MultipartFile logoImage,
            @RequestPart(required = false) MultipartFile[] images,
            @RequestPart(required = false) MultipartFile[] videos
    ) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        Boolean isCreated = technologyPostService.createPost(email, technologyPostDto, logoImage, images, videos);
        return ResponseEntity.ok(isCreated);
    }

    @PostMapping("create/restaurant")
    public ResponseEntity<Boolean> createRestaurantPost(
            @Valid @RequestPart RestaurantPostDto restaurantPostDto,
            @RequestPart(required = false) MultipartFile logoImage,
            @RequestPart(required = false) MultipartFile[] images,
            @RequestPart(required = false) MultipartFile[] videos
    ) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        Boolean isCreated = restaurantPostService.createPost(email, restaurantPostDto, logoImage, images, videos);
        return ResponseEntity.ok(isCreated);
    }

    @PostMapping("create/education")
    public ResponseEntity<Boolean> createEducationPost(
            @Valid @RequestPart EducationPostDto educationPostDto,
            @RequestPart(required = false) MultipartFile logoImage,
            @RequestPart(required = false) MultipartFile[] images,
            @RequestPart(required = false) MultipartFile[] videos
    ) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        Boolean isCreated = educationPostService.createPost(email, educationPostDto, logoImage, images, videos);
        return ResponseEntity.ok(isCreated);
    }

    @PostMapping("create/lifestyle")
    public ResponseEntity<Boolean> createLifeStylePost(
            @Valid @RequestPart LifeStylePostDto lifeStylePostDto,
            @RequestPart(required = false) MultipartFile logoImage,
            @RequestPart(required = false) MultipartFile[] images,
            @RequestPart(required = false) MultipartFile[] videos
    ) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        Boolean isCreated = lifeStylePostService.createPost(email, lifeStylePostDto, logoImage, images, videos);
        return ResponseEntity.ok(isCreated);
    }

    @PostMapping("create/entertainment")
    public ResponseEntity<Boolean> createEntertainmentPost(
            @Valid @RequestPart EntertainmentPostDto entertainmentPostDto,
            @RequestPart(required = false) MultipartFile logoImage,
            @RequestPart(required = false) MultipartFile[] images,
            @RequestPart(required = false) MultipartFile[] videos
    ) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        Boolean isCreated = entertainmentPostService.createPost(email, entertainmentPostDto, logoImage, images, videos);
        return ResponseEntity.ok(isCreated);
    }
}
