package com.hyperalert.hyperalert.dto;

import lombok.Data;

@Data
public class GeoResponse {
    private String zip;
    private String name;
    private double lat;
    private double lon;
    private String country;
}