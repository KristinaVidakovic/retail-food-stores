package com.retail_food_stores.ws.service;

import com.retail_food_stores.ws.model.EstablishmentType;
import com.retail_food_stores.ws.model.Store;
import com.retail_food_stores.ws.repository.StoreRepository;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.data.domain.Pageable;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
public class StoreServiceImpl implements StoreService {

    static Logger log = LoggerFactory.getLogger(StoreServiceImpl.class);

    private final StoreRepository storeRepository;

    @Autowired
    StoreServiceImpl(StoreRepository storeRepository) {
        this.storeRepository = storeRepository;
    }

    @Override
    public void save(MultipartFile csvFile) {
        log.info("Called method for saving all data from file");
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

    private List<Store> loadData(InputStream inputStream) {
        BufferedReader fileReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        CSVParser csvParser = null;
        try {
            csvParser = new CSVParser(fileReader,
                    CSVFormat.Builder.create().setHeader().setSkipHeaderRecord(true)
                            .setIgnoreHeaderCase(true).setTrim(true).build());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        List<Store> data = new ArrayList<Store>();

        Iterable<CSVRecord> csvRecords = csvParser.getRecords();

        for (CSVRecord csvRecord : csvRecords) {
            String licenseNumber = csvRecord.get("License_Number");
            String zipCode = csvRecord.get("Zip_Code");
            String squareFootage = csvRecord.get("Square_Footage");
            String latitude = csvRecord.get("Latitude");
            String longitude = csvRecord.get("Longitude");
            Store store = new Store(null,
                    csvRecord.get("County"),
                    licenseNumber != null && !licenseNumber.equals("") ?
                            Long.parseLong(licenseNumber):null,
                    EstablishmentType.fromValue(csvRecord.get("Establishment_Type")),
                    csvRecord.get("Entity_Name"),
                    csvRecord.get("DBA_Name"),
                    csvRecord.get("Street_Number"),
                    csvRecord.get("Street_Name"),
                    csvRecord.get("City"),
                    csvRecord.get("State_Abbreviation"),
                    zipCode != null && !zipCode.equals("") ?
                            Integer.parseInt(zipCode):null,
                    squareFootage != null && !squareFootage.equals("") ?
                            Integer.parseInt(squareFootage):null,
                    latitude != null && !latitude.equals("") ?
                            Double.parseDouble(latitude):null,
                    longitude != null && !longitude.equals("") ?
                            Double.parseDouble(longitude):null
            );

            data.add(store);
        }

        return data;

    }

}
