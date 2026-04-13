package com.hospital.pharmacy.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = {
        "http://localhost:5173",
        "http://127.0.0.1:5173",
        "http://localhost:3000",
        "http://127.0.0.1:3000"
})
@RequestMapping("/api/pharmacy")
public class PharmacyController {

    @GetMapping("/details")
    public ResponseEntity<Map<String, Object>> getPharmacyDetails() {
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("title", "Pharmacy Details");
        payload.put("description", "Central pharmacy view for medicine availability and delivery support across all hospital services.");
        payload.put("summary", Map.of(
                "totalMedicines", 312,
                "lowStockItems", 18,
                "todayDispensed", 146,
                "criticalAvailable", 42
        ));
        payload.put("services", List.of(
                createService("OP Pharmacy", "Fast issue counter for outpatient prescriptions and refill support."),
                createService("IP Ward Supply", "Scheduled medicine delivery for inpatients and nursing stations."),
                createService("Critical Medicines", "Priority stock for ICU and emergency treatment requirements."),
                createService("Inventory Batches", "Batch-wise tracking with expiry monitoring and low-stock alerts.")
        ));

        return ResponseEntity.ok(payload);
    }

    private Map<String, String> createService(String title, String detail) {
        return Map.of("title", title, "detail", detail);
    }
}
