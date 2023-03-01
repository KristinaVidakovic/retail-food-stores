package com.retail_food_stores.ws.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.retail_food_stores.ws.ObjectMother;
import com.retail_food_stores.ws.exceptions.UnsupportedFileException;
import com.retail_food_stores.ws.model.Store;
import com.retail_food_stores.ws.service.StoreService;
import org.junit.Before;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(StoreController.class)
public class StoreControllerTest {

    @MockBean
    private StoreService storeService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    @DisplayName("Testing save -> Loads and saves data successfully and returns status 200 OK")
    public void testLoadAndSave_Success() throws Exception {
        MockMultipartFile file = ObjectMother.createFile();

        mockMvc.perform(multipart("/load").file(file))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Successfully loaded data"));

    }

    @Test
    @DisplayName("Testing save -> Throws Unsupported File Exception and returns status 400 BAD REQUEST")
    public void testLoadAndSave_UnsupportedFileException() throws Exception {
        MockMultipartFile file = ObjectMother.createWrongFile();

        doThrow(new UnsupportedFileException()).when(storeService).save(anyList());

        mockMvc.perform(multipart("/load").file(file))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Unsupported file provided! You need to provide CSV file!"));
    }

    @Test
    @DisplayName("Testing save -> Throws Exception and returns status 417 EXPECTATION FAILED")
    public void testLoadAndSave_Exception() throws Exception {
        MockMultipartFile file = ObjectMother.createFile();

        doThrow(new RuntimeException()).when(storeService).save(anyList());

        mockMvc.perform(multipart("/load").file(file))
                .andExpect(status().isExpectationFailed());
    }

    @Test
    @DisplayName("Testing filter -> Returns list of stores by filter successfully and status 200 OK")
    public void testGetByFilter() throws Exception {
        Page<Store> stores = new PageImpl<>(Collections.singletonList(ObjectMother.createStore()));
        given(storeService.filter(anyString(), any(Pageable.class))).willReturn(stores);

        mockMvc.perform(get("/filter")
                        .param("filter", "name")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].id").exists());
    }

    @Test
    @DisplayName("Testing filter -> Returns empty body and status 204 NO CONTENT")
    public void testGetByFilter_NoContent() throws Exception {
        Page<Store> stores = new PageImpl<>(new ArrayList<>());
        given(storeService.filter(anyString(), any(Pageable.class))).willReturn(stores);

        mockMvc.perform(get("/filter")
                        .param("filter", "name")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Testing nearest -> Returns n nearest stores by longitude and latitude and status 200 OK")
    void getNearest() throws Exception {
        Double longitude = 70.123456;
        Double latitude = 44.23400;
        Integer numberOfStores = 1;
        List<Store> stores = List.of(ObjectMother.createStore());
        when(storeService.nearest(longitude, latitude, numberOfStores)).thenReturn(stores);

        MvcResult result = mockMvc.perform(get("/nearest")
                        .param("longitude", longitude.toString())
                        .param("latitude", latitude.toString())
                        .param("numberOfStores", numberOfStores.toString()))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        List<Store> responseStores = new ObjectMapper().readValue(jsonResponse, new TypeReference<List<Store>>(){});
        assertEquals(stores.size(), responseStores.size());
    }

    @Test
    @DisplayName("Testing nearest -> Returns status 400 BAD REQUEST")
    void getNearestLocation_ReturnsBadRequest() throws Exception {
        Double latitude = 44.23400;
        Integer numberOfStores = 1;

        mockMvc.perform(get("/nearest")
                        .param("longitude", "")
                        .param("latitude", latitude.toString())
                        .param("numberOfStores", numberOfStores.toString()))
                .andExpect(status().isBadRequest());
    }
}

