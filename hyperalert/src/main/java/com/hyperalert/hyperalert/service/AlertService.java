package com.hyperalert.hyperalert.service;

import com.hyperalert.hyperalert.entity.Alert;
import com.hyperalert.hyperalert.repository.AlertRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlertService {

    @Autowired
    private AlertRepository alertRepository;

    public Alert createAlert(Alert alert) {
        return alertRepository.save(alert);
    }

    public List<Alert> getAllAlerts() {
        return alertRepository.findAll();
    }

    public Alert getAlertById(Long id) {
        return alertRepository.findById(id).orElse(null);
    }

    public void deleteAlert(Long id) {
        alertRepository.deleteById(id);
    }
    public Alert updateAlert(Long id, Alert updatedAlert) {
        Alert existingAlert = alertRepository.findById(id).orElse(null);

        if (existingAlert != null) {
            existingAlert.setTitle(updatedAlert.getTitle());
            existingAlert.setDescription(updatedAlert.getDescription());
            existingAlert.setLocation(updatedAlert.getLocation());
            existingAlert.setSeverity(updatedAlert.getSeverity());
            existingAlert.setStatus(updatedAlert.getStatus());
            existingAlert.setAlertType(updatedAlert.getAlertType());
            return alertRepository.save(existingAlert);
        }
        return null;
    }
}