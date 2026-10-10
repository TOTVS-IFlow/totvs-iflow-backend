package org.example.controller;

import org.example.service.ClienteService;
import org.example.dto.ClienteResumoDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/clients")
public class ClienteController {

    private final ClienteService clienteService = new ClienteService();

    @GetMapping
    public List<ClienteResumoDTO> listarTodos() {
        return clienteService.listarResumoParaApi();
    }
}