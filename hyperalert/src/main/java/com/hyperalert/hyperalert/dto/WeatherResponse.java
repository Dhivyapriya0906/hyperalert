package com.hyperalert.hyperalert.dto;

import lombok.Data;

@Data
public class WeatherResponse {
    private Main main;
    private Weather[] weather;
    private String name;

    @Data
    public static class Main {
        private double temp;
        private double humidity;
        private double pressure;
    }

    @Data
    public static class Weather {
        private String main;
        private String description;
    }
}