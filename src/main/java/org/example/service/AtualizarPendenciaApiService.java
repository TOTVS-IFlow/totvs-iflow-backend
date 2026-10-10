package org.example.service;

import org.example.dao.PendenciaDAO;
import org.example.dto.PendenciaResumoDTO;
import org.example.exception.RecursoNaoEncontradoException;
import org.example.model.Pendencia;

import java.time.LocalDateTime;

public class AtualizarPendenciaApiService {

    private final PendenciaDAO pendenciaDAO = new PendenciaDAO();

    public PendenciaResumoDTO atualizarStatus(int id, String status) {

        if (status == null ||
                (!status.equals("open") && !status.equals("done"))) {
            throw new IllegalArgumentException(
                    "Status inválido. Use 'open' ou 'done'."
            );
        }

        Pendencia pendencia = pendenciaDAO.buscarPorId(id);

        if (pendencia == null) {
            throw new RecursoNaoEncontradoException(
                    "Pendência não encontrada."
            );
        }

        pendencia.setStatus(status);

        if (status.equals("done")) {
            if (pendencia.getDataConclusao() == null) {
                pendencia.setDataConclusao(LocalDateTime.now());
            }
        } else {
            pendencia.setDataConclusao(null);
        }

        pendenciaDAO.atualizar(pendencia);

        return new PendenciaResumoDTO(
                pendencia.getId(),
                pendencia.getDescricao(),
                pendencia.getResponsavel(),
                pendencia.getStatus(),
                pendencia.getReuniao().getId(),
                pendencia.getReuniao().getCliente().getNome()
        );
    }
}