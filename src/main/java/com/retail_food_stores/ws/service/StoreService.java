package com.retail_food_stores.ws.service;

import com.retail_food_stores.ws.model.Store;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.data.domain.Pageable;

import java.util.List;

public interface StoreService {

    void save(List<MultipartFile> csvFiles);

    Page<Store> filter(String filter, Pageable pageable);

    List<Store> nearest(Double longitude, Double latitude, Integer numberOfStores);

}
