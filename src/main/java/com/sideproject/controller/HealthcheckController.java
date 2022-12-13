package com.sideproject.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthcheckController {

    @GetMapping("/healthcheck")
    public HealthStatus healthCheck() {
        return new HealthStatus("ok");
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
