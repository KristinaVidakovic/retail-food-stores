package com.retail_food_stores.ws.controller;

import com.retail_food_stores.ws.model.Store;
import com.retail_food_stores.ws.service.StoreService;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.data.domain.Pageable;

import java.util.List;

@RestController
public class StoreController {

    private final StoreService storeService;

    @Autowired
    StoreController (StoreService storeService) {
        this.storeService = storeService;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> save (@RequestParam MultipartFile csvFile) {
        try {
            storeService.save(csvFile);
            return ResponseEntity.status(HttpStatus.OK).body("Successfully uploaded data");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(e.getMessage());
        }
    }

    @GetMapping("/filter")
    public ResponseEntity<Page<Store>> filter (@RequestParam String filter,
                                               @ParameterObject @PageableDefault(size = 20, page = 1) Pageable pageable) {
        Page<Store> stores = storeService.filter(filter, pageable);
        if (stores.hasContent()) {
            return ResponseEntity.status(HttpStatus.OK).body(stores);
        } else {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        }
    }


    @GetMapping("/nearest")
    public ResponseEntity<List<Store>> getNearestLocation(@RequestParam("longitude") Double longitude,
                                                          @RequestParam("latitude") Double latitude,
                                                          @RequestParam("numberOfStores") Integer numberOfStores) {

        return ResponseEntity.status(HttpStatus.OK).body(storeService.nearest(longitude, latitude, numberOfStores));

    }

}
