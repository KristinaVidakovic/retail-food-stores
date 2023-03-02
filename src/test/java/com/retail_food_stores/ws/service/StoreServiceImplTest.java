package com.retail_food_stores.ws.service;

import com.retail_food_stores.ws.ObjectMother;
import com.retail_food_stores.ws.exceptions.UnsupportedFileException;
import com.retail_food_stores.ws.model.EstablishmentType;
import com.retail_food_stores.ws.model.Store;
import com.retail_food_stores.ws.repository.StoreRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class StoreServiceImplTest {

    @Mock
    private StoreRepository storeRepository;
    private StoreServiceImpl storeService;

    @Before
    public void setup() {
        storeRepository = mock(StoreRepository.class);
        storeService = new StoreServiceImpl(storeRepository);
    }
    @Test
    @DisplayName("Testing save -> Saves all data successfully")
    public void testSave() throws IOException {
        MultipartFile file = mock(MultipartFile.class);
        List<MultipartFile> csvFiles = new ArrayList<>();
        csvFiles.add(file);
        when(file.getOriginalFilename()).thenReturn("test.csv");
        InputStream inputStream = mock(InputStream.class);
        when(file.getInputStream()).thenReturn(inputStream);

        storeService.save(csvFiles);

        verify(storeRepository, times(1)).saveAll(anyList());
    }

    @Test
    @DisplayName("Testing save -> Throws Unsupported File Exception")
    public void save_UnsupportedFileException() {
        MultipartFile unsupportedFile = mock(MultipartFile.class);
        when(unsupportedFile.getOriginalFilename()).thenReturn("test.txt");

        assertThrows(UnsupportedFileException.class, () -> storeService.loadFile(unsupportedFile));
    }

    @Test
    @DisplayName("Testing loadFile -> Loads file successfully")
    public void testLoadFile() throws IOException {
        MultipartFile csvFile = mock(MultipartFile.class);
        InputStream inputStream = ObjectMother.createInputStream();
        when(csvFile.getOriginalFilename()).thenReturn("test.csv");
        when(csvFile.getInputStream()).thenReturn(inputStream);

        storeService.loadFile(csvFile);

        verify(storeRepository, times(1)).saveAll(anyList());
    }

    @Test
    @DisplayName("Testing loadFile -> Throws Unsupported File Exception")
    public void loadFile_UnsupportedFile() {
        MultipartFile file = mock(MultipartFile.class);
        when(file.getOriginalFilename()).thenReturn("test.txt");

        assertThrows(UnsupportedFileException.class, () -> storeService.loadFile(file));
    }

    @Test
    @DisplayName("Testing loadFile -> Throws Runtime Exception")
    public void loadFile_MalformedCSV() throws IOException {
        MultipartFile csvFile = mock(MultipartFile.class);
        InputStream inputStream = ObjectMother.createMalformedInputStream();
        when(csvFile.getOriginalFilename()).thenReturn("test.csv");
        when(csvFile.getInputStream()).thenReturn(inputStream);

        assertThrows(RuntimeException.class, () -> storeService.loadFile(csvFile));
    }

    @Test
    @DisplayName("Testing filter -> Returns data by filter successfully")
    public void testFilter() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Store> stores = new PageImpl<>(Collections.singletonList(ObjectMother.createStore()));
        when(storeRepository.findByEntityNameContainingOrStreetNameContaining(anyString(), anyString(), any())).thenReturn(stores);

        Page<Store> result = storeService.filter("name", pageable);

        verify(storeRepository).findByEntityNameContainingOrStreetNameContaining("name", "name", pageable);
        assertEquals(stores, result);
    }

    @Test
    @DisplayName("Testing neatest -> Returns list of nearest stores successfully")
    public void testNearest() {
        double longitude = -77.0366;
        double latitude = 38.8951;
        int numberOfStores = 1;
        List<Store> stores = Collections.singletonList(ObjectMother.createStore());
        when(storeRepository.findByLongitudeNotNullAndLatitudeNotNull()).thenReturn(stores);

        List<Store> result = storeService.nearest(longitude, latitude, numberOfStores);

        verify(storeRepository).findByLongitudeNotNullAndLatitudeNotNull();
        assertEquals(stores.get(0), result.get(0));
        assertEquals(stores.size(), result.size());
    }

    @Test
    @DisplayName("Testing loadData -> Successfully loads data")
    public void testLoadData() throws IOException {
        InputStream inputStream = ObjectMother.createInputStream();

        List<Store> data = storeService.loadData(inputStream);

        assertEquals(1, data.size());
        Store store = data.get(0);
        assertEquals("AL", store.getCounty());
        assertEquals(Long.valueOf(11), store.getLicenseNumber());
        assertEquals(EstablishmentType.A, store.getEstablishmentType());
        assertEquals("Store A", store.getEntityName());
        assertEquals("dba", store.getDbaName());
        assertEquals("1", store.getStreetNumber());
        assertEquals("name", store.getStreetName());
        assertEquals("LL", store.getCity());
        assertEquals("NY", store.getStateAbbreviation());
        assertEquals(Integer.valueOf(1111), store.getZipCode());
        assertEquals(Integer.valueOf(1234), store.getSquareFootage());
        assertEquals(Double.valueOf(22.2345), store.getLatitude());
        assertEquals(Double.valueOf(11.3456), store.getLongitude());
    }
}
