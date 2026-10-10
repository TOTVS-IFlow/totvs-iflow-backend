package org.example.service;

import org.example.dao.PendenciaDAO;
import org.example.dao.ReuniaoDAO;
import org.example.dto.ReuniaoResumoDTO;
import org.example.model.Pendencia;
import org.example.model.Reuniao;

import java.util.List;

public class ReuniaoApiService {

    private final ReuniaoDAO reuniaoDAO = new ReuniaoDAO();
    private final PendenciaDAO pendenciaDAO = new PendenciaDAO();

    public List<ReuniaoResumoDTO> listar(Integer clientId) {

        List<Reuniao> reunioes;

        if (clientId == null) {
            reunioes = reuniaoDAO.buscarTodos();
        } else {
            reunioes = reuniaoDAO.buscarPorClienteId(clientId);
        }

        return reunioes.stream().map(reuniao -> {
            List<Pendencia> pendencias =
                    pendenciaDAO.buscarPorReuniaoId(reuniao.getId());

            int abertas = (int) pendencias.stream()
                    .filter(p -> "open".equalsIgnoreCase(p.getStatus()))
                    .count();

            return new ReuniaoResumoDTO(
                    reuniao.getId(),
                    reuniao.getCliente().getNome(),
                    reuniao.getTitulo(),
                    reuniao.getData(),
                    reuniao.getStatus(),
                    reuniao.getSentimento(),
                    abertas
            );
        }).toList();
    }
}