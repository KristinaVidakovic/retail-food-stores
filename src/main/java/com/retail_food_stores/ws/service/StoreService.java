package com.retail_food_stores.ws.service;

import com.retail_food_stores.ws.model.Store;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.data.domain.Pageable;

public interface StoreService {

    void save(MultipartFile csvFile);

    Page<Store> filter(String filter, Pageable pageable);
}
