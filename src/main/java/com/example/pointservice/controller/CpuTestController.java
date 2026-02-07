package com.example.pointservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CpuTestController {
    @GetMapping("/api/points/cpu-test")
    public String cpuTest() {
        long sum = 0;
        for (long i = 0; i < 500_000_000L; i++) {
            sum += i;
        }
        return "ok";
    }
}
