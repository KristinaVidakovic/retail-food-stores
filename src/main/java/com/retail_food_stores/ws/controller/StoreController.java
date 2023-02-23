package com.retail_food_stores.ws.controller;

import com.retail_food_stores.ws.exceptions.UnsupportedFileException;
import com.retail_food_stores.ws.model.ResponseMessage;
import com.retail_food_stores.ws.model.Store;
import com.retail_food_stores.ws.service.StoreService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.data.domain.Pageable;

import java.util.List;

@RestController
public class StoreController {

    private final StoreService storeService;

    @Autowired
    StoreController (StoreService storeService) {
        this.storeService = storeService;
    }

    @Operation(summary = "Load csv file and save data in database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully loaded data",
                    content = { @Content(mediaType = "application/json",
                        schema = @Schema(implementation = ResponseMessage.class)) }),
            @ApiResponse(responseCode = "400", description = "Unsupported file provided",
                    content = { @Content(mediaType = "application/json",
                        schema = @Schema(implementation = UnsupportedFileException.class)) }),
            @ApiResponse(responseCode = "417", description = "Expectation failed",
                    content = { @Content(mediaType = "application/json",
                        schema = @Schema(implementation = ResponseMessage.class)) })
    })
    @PostMapping("/upload")
    public ResponseEntity<?> loadAndSave (
            @Parameter(description = "CSV file with data to be stored") @RequestParam MultipartFile csvFile) {

        try {
            storeService.save(csvFile);
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(HttpStatus.OK, "Successfully loaded data"));
        } catch (UnsupportedFileException e) {
            return ResponseEntity.status(UnsupportedFileException.HTTP_STATUS).body(e);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(HttpStatus.EXPECTATION_FAILED, e.getMessage()));
        }
    }

    @Operation(summary = "Get list of stores by provided filter")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully returned data by filter",
                content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Page.class)) }),
            @ApiResponse(responseCode = "201", description = "No data found for provided filter")
    })
    @GetMapping("/filter")
    public ResponseEntity<?> getByFilter (
            @Parameter(description = "Partially or full entity or street name to be searched") @RequestParam String filter,
            @ParameterObject @PageableDefault(size = 20, page = 1) Pageable pageable) {
        Page<Store> stores = storeService.filter(filter, pageable);
        if (stores.hasContent()) {
            return ResponseEntity.status(HttpStatus.OK).body(stores);
        } else {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
    }

    @Operation(summary = "Get certain number of nearest stores by longitude and latitude")
    @ApiResponse(responseCode = "200", description = "Successfully returned data by longitude and latitude",
        content = { @Content(mediaType = "application/json",
            schema = @Schema(implementation = List.class)) })
    @GetMapping("/nearest")
    public ResponseEntity<List<Store>> getNearestLocation(
            @Parameter(description = "Longitude to be searched") @RequestParam("longitude") Double longitude,
            @Parameter(description = "Latitude to be searched") @RequestParam("latitude") Double latitude,
            @Parameter(description = "Number of nearest stores to be returned") @RequestParam("numberOfStores") Integer numberOfStores) {

        return ResponseEntity.status(HttpStatus.OK).body(storeService.nearest(longitude, latitude, numberOfStores));
    }

}
