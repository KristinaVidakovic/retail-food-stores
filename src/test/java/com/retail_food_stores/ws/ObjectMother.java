package com.retail_food_stores.ws;

import com.retail_food_stores.ws.model.EstablishmentType;
import com.retail_food_stores.ws.model.Store;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
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

    public static InputStream createInputStream() {
        return new ByteArrayInputStream("county,licenseNumber,establishmentType,entityName,dbaName,streetNumber,streetName,city,stateAbbreviation,zipCode,squareFootage,latitude,longitude\nAL,11,A,Store A,dba,1,name,LL,NY,1111,1234,22.2345,11.3456".getBytes());
    }

    public static InputStream createMalformedInputStream() {
        return new ByteArrayInputStream("county,licenseNumber,establishmentType,entityName,dbaName,streetNumber,streetName,city,stateAbbreviation,zipCode,squareFootage,latitude,longitude\nAL,11,A,Store A,dba,1,name,LL,NY,1111,1234,22.2345,11.3456\n2".getBytes());
    }
}
