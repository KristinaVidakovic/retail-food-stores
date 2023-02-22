package com.retail_food_stores.ws.service;

import org.springframework.web.multipart.MultipartFile;

public interface StoreService {

    void save(MultipartFile csvFile);
}
