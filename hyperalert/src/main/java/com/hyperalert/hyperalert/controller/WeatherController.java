package com.hyperalert.hyperalert.controller;

import com.hyperalert.hyperalert.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/weather")
public class WeatherController {

    @Autowired
    private WeatherService weatherService;

    @GetMapping("/{pincode}")
    public Map<String, Object> getWeather(@PathVariable String pincode) {
        return weatherService.getWeatherByPincode(pincode);
    }
}