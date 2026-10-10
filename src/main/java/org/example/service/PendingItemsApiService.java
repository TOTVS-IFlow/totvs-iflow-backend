package org.example.service;

import org.example.dao.PendenciaDAO;
import org.example.dto.PendenciaResumoDTO;
import org.example.model.Pendencia;

import java.util.List;

public class PendingItemsApiService {

    private final PendenciaDAO pendenciaDAO = new PendenciaDAO();

    public List<PendenciaResumoDTO> listar(String status, Integer clientId) {

        List<Pendencia> pendencias =
                pendenciaDAO.buscarParaApi(status, clientId);

        return pendencias.stream()
                .map(p -> new PendenciaResumoDTO(
                        p.getId(),
                        p.getDescricao(),
                        p.getResponsavel(),
                        p.getStatus(),
                        p.getReuniao().getId(),
                        p.getReuniao().getCliente().getNome()
                ))
                .toList();
    }
}