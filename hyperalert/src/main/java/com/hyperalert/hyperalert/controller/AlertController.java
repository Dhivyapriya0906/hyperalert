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
        String username = authentication.getName();
        if (existing.getCreatedBy() != null
                && !existing.getCreatedBy().equals("SYSTEM")
                && !existing.getCreatedBy().equals(username)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("You don't have permission to update this alert");
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
        String username = authentication.getName();
        if (existing.getCreatedBy() != null
                && !existing.getCreatedBy().equals("SYSTEM")
                && !existing.getCreatedBy().equals(username)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("You don't have permission to delete this alert");
        }
        alertService.deleteAlert(id);
        return ResponseEntity.ok("Alert deleted successfully");
    }
    @PostMapping
    public Alert createAlert(@Valid @RequestBody Alert alert, Authentication authentication) {
        alert.setCreatedBy(authentication.getName());
        return alertService.createAlert(alert);
    }

}