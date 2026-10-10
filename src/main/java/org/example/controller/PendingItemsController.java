package org.example.controller;

import org.example.dto.PendenciaResumoDTO;
import org.example.dto.AtualizarStatusDTO;
import org.example.service.AtualizarPendenciaApiService;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.example.service.PendingItemsApiService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/pending-items")
public class PendingItemsController {

    private final PendingItemsApiService service =
            new PendingItemsApiService();

    @GetMapping
    public List<PendenciaResumoDTO> listar(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Integer clientId) {

        return service.listar(status, clientId);
    }

    private final AtualizarPendenciaApiService atualizarService =
            new AtualizarPendenciaApiService();

    @PatchMapping("/{id}")
    public PendenciaResumoDTO atualizarStatus(
            @PathVariable int id,
            @RequestBody AtualizarStatusDTO requisicao) {

        return atualizarService.atualizarStatus(
                id,
                requisicao.getStatus()
        );
    }
}