package org.example.controller;

import org.example.dto.ReuniaoDetalheDTO;
import org.example.dto.ReuniaoResumoDTO;
import org.example.service.ReuniaoApiService;
import org.example.service.ReuniaoDetalheApiService;
import org.example.dto.CriarReuniaoDTO;
import org.example.service.CriarReuniaoApiService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@RestController
@RequestMapping("/meetings")
public class ReuniaoController {

    private final ReuniaoApiService reuniaoService =
            new ReuniaoApiService();

    @GetMapping
    public List<ReuniaoResumoDTO> listar(
            @RequestParam(required = false) Integer clientId) {
        return reuniaoService.listar(clientId);
    }

    private final ReuniaoDetalheApiService detalheService =
            new ReuniaoDetalheApiService();

    @GetMapping("/{id}")
    public ReuniaoDetalheDTO buscarPorId(@PathVariable int id) {
        return detalheService.buscarPorId(id);
    }

    private final CriarReuniaoApiService criarService =
            new CriarReuniaoApiService();

    @PostMapping
    public ResponseEntity<ReuniaoDetalheDTO> criar(
            @RequestBody CriarReuniaoDTO dados) {

        int id = criarService.criar(dados);

        ReuniaoDetalheDTO reuniao =
                detalheService.buscarPorId(id);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(reuniao);
    }
}
