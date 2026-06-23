package com.hyperalert.hyperalert.controller;

import com.hyperalert.hyperalert.entity.Alert;
import com.hyperalert.hyperalert.service.AlertService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/alerts")
public class AlertController {

    private final AlertService alertService;

    public AlertController(AlertService alertService) {
        this.alertService = alertService;
    }

    @PostMapping
    public Alert createAlert(@Valid @RequestBody Alert alert) {
        return alertService.createAlert(alert);
    }

    @GetMapping
    public List<Alert> getAllAlerts() {
        return alertService.getAllAlerts();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getAlertById(@PathVariable Long id, Authentication authentication) {
        Alert alert = alertService.getAlertById(id);
        if (alert == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Alert not found with id: " + id);
        }
        return ResponseEntity.ok(alert);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateAlert(@PathVariable Long id,
                                         @RequestBody Alert alert,
                                         Authentication authentication) {
        Alert existing = alertService.getAlertById(id);
        if (existing == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Alert not found with id: " + id);
        }
        return ResponseEntity.ok(alertService.updateAlert(id, alert));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAlert(@PathVariable Long id, Authentication authentication) {
        Alert existing = alertService.getAlertById(id);
        if (existing == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Alert not found with id: " + id);
        }
        alertService.deleteAlert(id);
        return ResponseEntity.ok("Alert deleted successfully");
    }
}