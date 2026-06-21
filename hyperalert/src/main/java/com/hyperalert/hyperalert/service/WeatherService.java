package com.hyperalert.hyperalert.service;

import com.hyperalert.hyperalert.dto.GeoResponse;
import com.hyperalert.hyperalert.dto.PincodeResponse;
import com.hyperalert.hyperalert.dto.WeatherResponse;
import com.hyperalert.hyperalert.entity.Alert;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;

import java.util.HashMap;
import java.util.Map;

@Service
public class WeatherService {

    @Value("${openweather.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate;
    private final AlertService alertService;
    private final EmailService emailService;

    public WeatherService(RestTemplate restTemplate, AlertService alertService, EmailService emailService) {
        this.restTemplate = restTemplate;
        this.alertService = alertService;
        this.emailService = emailService;
    }

    public Map<String, Object> getWeatherByPincode(String pincode) {
        // Validate pincode format (must be 6 digits)
        if (pincode == null || !pincode.matches("\\d{6}")) {
            throw new IllegalArgumentException("Invalid pincode format. Must be 6 digits.");
        }
        // Step 1: Get official village/post office name from India Post API
        String pincodeUrl = "https://api.postalpincode.in/pincode/" + pincode;
        HttpHeaders headers = new HttpHeaders();
        headers.set("User-Agent", "Mozilla/5.0");
        headers.set("Accept", "application/json");
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<PincodeResponse[]> responseEntity = restTemplate.exchange(
                pincodeUrl, HttpMethod.GET, entity, PincodeResponse[].class
        );
        PincodeResponse[] pincodeResponse = responseEntity.getBody();
        String officialName = "Unknown";
        String district = "Unknown";
        if (pincodeResponse != null && pincodeResponse.length > 0
                && "Success".equals(pincodeResponse[0].getStatus())
                && pincodeResponse[0].getPostOffice() != null
                && !pincodeResponse[0].getPostOffice().isEmpty()) {
            officialName = pincodeResponse[0].getPostOffice().get(0).getName();
            district = pincodeResponse[0].getPostOffice().get(0).getDistrict();
        }

        // Step 2: Get lat/lon from OpenWeatherMap
        // Step 2: Get lat/lon from OpenWeatherMap
        String geoUrl = "https://api.openweathermap.org/geo/1.0/zip?zip="
                + pincode + ",IN&appid=" + apiKey;
        GeoResponse geoResponse;
        try {
            geoResponse = restTemplate.getForObject(geoUrl, GeoResponse.class);
        } catch (Exception e) {
            throw new RuntimeException("Invalid pincode or weather data unavailable for: " + pincode);
        }

        if (geoResponse == null || geoResponse.getLat() == 0) {
            throw new RuntimeException("Could not find location for pincode: " + pincode);
        }

        // Step 3: Get weather using lat/lon
        String weatherUrl = "https://api.openweathermap.org/data/2.5/weather?lat="
                + geoResponse.getLat() + "&lon=" + geoResponse.getLon()
                + "&appid=" + apiKey + "&units=metric";
        WeatherResponse weatherResponse = restTemplate.getForObject(weatherUrl, WeatherResponse.class);

        // Step 4: Combine everything into one clean response
        Map<String, Object> result = new HashMap<>();
        result.put("requestedPincode", pincode);
        result.put("officialLocationName", officialName);
        result.put("district", district);
        result.put("nearestWeatherStation", geoResponse.getName());
        result.put("temperature", weatherResponse.getMain().getTemp());
        result.put("humidity", weatherResponse.getMain().getHumidity());
        result.put("weatherCondition", weatherResponse.getWeather()[0].getMain());
        result.put("description", weatherResponse.getWeather()[0].getDescription());

        // Step 5: Check for severe weather and auto-create alert
        double temp = weatherResponse.getMain().getTemp();
        String weatherMain = weatherResponse.getWeather()[0].getMain();
        String severeCondition = checkForSevereCondition(temp, weatherMain);

        if (severeCondition != null) {
            Alert autoAlert = new Alert();
            autoAlert.setTitle(severeCondition + " Alert - " + officialName);
            autoAlert.setDescription("Auto-generated alert: " + weatherResponse.getWeather()[0].getDescription()
                    + ", Temp: " + temp + "°C");
            autoAlert.setLocation(officialName + ", " + district);
            autoAlert.setSeverity(severeCondition.equals("HEATWAVE") ? "HIGH" : "MEDIUM");
            autoAlert.setStatus("ACTIVE");
            autoAlert.setAlertType(severeCondition);

            alertService.createAlert(autoAlert);

            emailService.sendAlertEmail(
                    "indiradhivyapriya@gmail.com",
                    severeCondition + " Alert: " + officialName,
                    "Emergency Alert!\n\nLocation: " + officialName + ", " + district
                            + "\nCondition: " + weatherResponse.getWeather()[0].getDescription()
                            + "\nTemperature: " + temp + "°C"
                            + "\n\nStay safe and follow local authority guidance."
            );

            result.put("alertGenerated", true);
            result.put("alertType", severeCondition);
        } else {
            result.put("alertGenerated", false);
        }

        return result;
    }

    private String checkForSevereCondition(double temp, String weatherMain) {
        // Note: This logic uses OpenWeatherMap conditions as a proxy.
        // Architecture is IMD-API-ready — swap this method's internals
        // with official IMD cyclone/flood bulletins once API access is approved.

        if (temp > 45) {
            return "EXTREME_HEATWAVE";
        }
        if (temp > 40) {
            return "HEATWAVE";
        }
        if (temp < 5) {
            return "COLD_WAVE";
        }
        if (weatherMain.equalsIgnoreCase("Tornado")) {
            return "CYCLONE_WARNING";
        }
        if (weatherMain.equalsIgnoreCase("Thunderstorm")) {
            return "STORM";
        }
        if (weatherMain.equalsIgnoreCase("Rain") || weatherMain.equalsIgnoreCase("Drizzle")) {
            return "FLOOD_RISK";
        }
        if (weatherMain.equalsIgnoreCase("Squall")) {
            return "HIGH_WIND_WARNING";
        }
        return null;
    }
}