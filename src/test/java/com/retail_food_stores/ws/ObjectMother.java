package com.retail_food_stores.ws;

import com.retail_food_stores.ws.model.EstablishmentType;
import com.retail_food_stores.ws.model.Store;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import java.util.UUID;

public class ObjectMother {
    public static Store createStore() {
        return new Store(UUID.randomUUID(),
                "County",
                11L,
                EstablishmentType.A,
                "Entity name",
                "DBA name",
                "Street number",
                "Street name",
                "City",
                "State",
                11111,
                1234,
                70.123456,
                44.23456);
    }

    public static MockMultipartFile createFile() {
        return new MockMultipartFile("csvFiles",
                "test.csv",
                MediaType.TEXT_PLAIN_VALUE,
                "test data".getBytes());
    }

    public static MockMultipartFile createWrongFile() {
        return new MockMultipartFile("csvFiles",
                "test.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "test data".getBytes());
    }
}
