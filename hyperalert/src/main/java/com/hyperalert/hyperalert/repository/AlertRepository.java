package com.hyperalert.hyperalert.repository;

import com.hyperalert.hyperalert.entity.Alert;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlertRepository extends JpaRepository<Alert, Long> {
}