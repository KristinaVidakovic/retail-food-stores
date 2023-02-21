package com.retail_food_stores.ws.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "STORE")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Store {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "ID", nullable = false)
    private UUID id;

    @Column(name = "COUNTY")
    private String county;

    @Column(name = "LICENSE_NUMBER")
    private Long licenseNumber;

    @Column(name = "ESTABLISHMENT_TYPE")
    private EstablishmentType establishmentType;

    @Column(name = "ENTITY_NAME")
    private String entityName;

    @Column(name = "DBA_NAME")
    private String dbaName;

    @Column(name = "STREET_NUMBER")
    private String streetNumber;

    @Column(name = "STREET_NAME")
    private String streetName;

    @Column(name = "CITY")
    private String city;

    @Column(name = "STATE_ABBREVIATION")
    private String stateAbbreviation;

    @Column(name = "ZIP_CODE")
    private Integer zipCode;

    @Column(name = "SQUARE_FOOTAGE")
    private Integer squareFootage;

    @Column(name = "LATITUDE")
    private Double latitude;

    @Column(name = "LONGITUDE")
    private Double longitude;

    @Override
    public String toString() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(this);
    }
}
