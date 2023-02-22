package com.retail_food_stores.ws.controller;

import com.retail_food_stores.ws.service.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class StoreController {

    private final StoreService storeService;

    @Autowired
    StoreController (StoreService storeService) {
        this.storeService = storeService;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> save (@RequestParam("csvFile") MultipartFile csvFile) {
        try {
            storeService.save(csvFile);
            return ResponseEntity.status(HttpStatus.OK).body("Successfully uploaded data");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(e.getMessage());
        }
    }
}
