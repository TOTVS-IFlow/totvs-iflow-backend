package org.example.controller;

import org.example.dto.DashboardResumoDTO;
import org.example.service.DashboardService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DashboardController {

    private final DashboardService dashboardService =
            new DashboardService();

    @GetMapping("/dashboard/summary")
    public DashboardResumoDTO obterResumo(
            @RequestParam(required = false) Integer clientId) {

        return dashboardService.gerarResumo(clientId);
    }
}