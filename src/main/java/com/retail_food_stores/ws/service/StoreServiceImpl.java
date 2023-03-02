package com.retail_food_stores.ws.service;

import com.harium.storage.kdtree.KDTree;
import com.harium.storage.kdtree.KeyDuplicateException;
import com.harium.storage.kdtree.KeySizeException;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;
import com.retail_food_stores.ws.exceptions.UnsupportedFileException;
import com.retail_food_stores.ws.model.EstablishmentType;
import com.retail_food_stores.ws.model.Store;
import com.retail_food_stores.ws.repository.StoreRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.data.domain.Pageable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class StoreServiceImpl implements StoreService {

    static Logger log = LoggerFactory.getLogger(StoreServiceImpl.class);

    private final StoreRepository storeRepository;

    @Autowired
    StoreServiceImpl(StoreRepository storeRepository) {
        this.storeRepository = storeRepository;
    }

    @Override
    @Scheduled(fixedDelay = 60000)
    public void save(List<MultipartFile> csvFiles) {
        log.info("Called method for loading data from files");
        Map<Integer, List<MultipartFile>> fileBatches = IntStream.range(0, csvFiles.size())
                .boxed()
                .collect(Collectors.groupingBy(i -> i / 10,
                        Collectors.mapping(csvFiles::get, Collectors.toList())));

        fileBatches.values().stream()
                .parallel()
                .forEach(batch -> batch.forEach(file -> {
                    loadFile(file);
                    log.info("Successfully loaded file " + file.getOriginalFilename());
                }));

    }

    public void loadFile(MultipartFile csvFile) {
        if (!StringUtils.getFilenameExtension(csvFile.getOriginalFilename()).equals("csv")) {
            log.error("Unsupported file " + csvFile.getOriginalFilename() + " provided!");
            throw new UnsupportedFileException();
        }

        try {
            List<Store> data = loadData(csvFile.getInputStream());
            log.info("Loaded data from file");
            storeRepository.saveAll(data);
            log.info("Saved data from file");
        } catch (IOException e) {
            log.error("Fail to store data");
            throw new RuntimeException("Fail to store data: " + e.getMessage());
        }
    }

    @Override
    public Page<Store> filter(String filter, Pageable pageable) {
        log.info("Called method for getting stores by filter");
        return storeRepository.findByEntityNameContainingOrStreetNameContaining(filter, filter, pageable);
    }

    @Override
    public List<Store> nearest(Double longitude, Double latitude, Integer numberOfStores) {
        log.info("Called method for getting nearest stores");
        List<Store> stores = storeRepository.findByLongitudeNotNullAndLatitudeNotNull();
        Set<String> insertedKeys = new HashSet<>();
        KDTree kdTree = new KDTree(2, 1000L);
        stores.forEach(i -> {
            double[] point = { i.getLongitude(), i.getLatitude() };
            String key = point[0] + "," + point[1];

            if (!insertedKeys.contains(key)) {
                try {
                    kdTree.insert(point, i);
                } catch (KeySizeException | KeyDuplicateException e) {
                    throw new RuntimeException(e);
                }
                insertedKeys.add(key);
            }
        });

        double[] query = { longitude, latitude };
        List<Store> neighbors = new ArrayList<>();
        try {
             neighbors = kdTree.nearest(query, numberOfStores);
        } catch (KeySizeException e) {
            throw new RuntimeException(e);
        }
        return neighbors;
    }

    List<Store> loadData(InputStream inputStream) throws IOException {
        try (CSVReader csvReader = new CSVReaderBuilder(new InputStreamReader(inputStream, StandardCharsets.UTF_8))
                .withSkipLines(1)
                .build()) {

            List<Store> data = new ArrayList<>();
            String[] line;
            while ((line = csvReader.readNext()) != null) {
                String licenseNumber = line[1];
                String zipCode = line[9];
                String squareFootage = line[10];
                String latitude = line[11];
                String longitude = line[12];
                Store store = new Store(
                        null,
                        line[0],
                        licenseNumber != null && !licenseNumber.equals("") ?
                                Long.parseLong(licenseNumber) : null,
                        EstablishmentType.fromValue(line[2]),
                        line[3],
                        line[4],
                        line[5],
                        line[6],
                        line[7],
                        line[8],
                        zipCode != null && !zipCode.equals("") ?
                                Integer.parseInt(zipCode) : null,
                        squareFootage != null && !squareFootage.equals("") ?
                                Integer.parseInt(squareFootage) : null,
                        latitude != null && !latitude.equals("") ?
                                Double.parseDouble(latitude) : null,
                        longitude != null && !longitude.equals("") ?
                                Double.parseDouble(longitude) : null
                );
                data.add(store);
            }
            return data;
        } catch (IOException | CsvValidationException e) {
            throw new RuntimeException(e);
        }
    }

}
