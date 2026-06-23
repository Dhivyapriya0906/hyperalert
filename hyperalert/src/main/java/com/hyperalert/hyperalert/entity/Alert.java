package com.hyperalert.hyperalert.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Entity
@Table(name = "alerts")
@Data
public class Alert {
    private String createdBy; // username who created or "SYSTEM" for auto-generated
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Title is required")
    private String title;

    private String description;

    @NotBlank(message = "Location is required")
    private String location;

    private String severity;
    private String status;
    private String alertType;
}