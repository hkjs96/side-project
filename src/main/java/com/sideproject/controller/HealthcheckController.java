package com.sideproject.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthcheckController {

    @GetMapping("/healthcheck")
    public ResponseEntity<?> healthCheck() {
        ResponseEntity responseEntity = ResponseEntity.ok().body(new HealthStatus("ok").getStatus());
        return responseEntity;
    }

    class HealthStatus {
        private String status;

        HealthStatus(String status) {
            this.status = status;
        }

        String getStatus() {
            return status;
        }
    }
}
