package org.example.service;

import org.example.dao.OportunidadeDAO;
import org.example.dao.PendenciaDAO;
import org.example.dao.ReuniaoDAO;
import org.example.dao.RiscoDAO;
import org.example.dto.ReuniaoDetalheDTO;
import org.example.exception.RecursoNaoEncontradoException;
import org.example.model.Oportunidade;
import org.example.model.Pendencia;
import org.example.model.Reuniao;
import org.example.model.Risco;

import java.util.List;

public class ReuniaoDetalheApiService {

    private final ReuniaoDAO reuniaoDAO = new ReuniaoDAO();
    private final OportunidadeDAO oportunidadeDAO = new OportunidadeDAO();
    private final RiscoDAO riscoDAO = new RiscoDAO();
    private final PendenciaDAO pendenciaDAO = new PendenciaDAO();

    public ReuniaoDetalheDTO buscarPorId(int id) {
        Reuniao reuniao = reuniaoDAO.buscarPorId(id);

        if (reuniao == null) {
            throw new RecursoNaoEncontradoException(
                    "Reunião não encontrada."
            );
        }

        List<ReuniaoDetalheDTO.OportunidadeDTO> oportunidades =
                oportunidadeDAO.buscarPorReuniaoId(id).stream()
                        .map(o -> new ReuniaoDetalheDTO.OportunidadeDTO(
                                o.getTag(), o.getDescricao()))
                        .toList();

        List<ReuniaoDetalheDTO.RiscoDTO> riscos =
                riscoDAO.buscarPorReuniaoId(id).stream()
                        .map(r -> new ReuniaoDetalheDTO.RiscoDTO(
                                r.getNivel(), r.getDescricao()))
                        .toList();

        List<ReuniaoDetalheDTO.PendenciaDTO> pendencias =
                pendenciaDAO.buscarPorReuniaoId(id).stream()
                        .map(p -> new ReuniaoDetalheDTO.PendenciaDTO(
                                p.getId(),
                                p.getDescricao(),
                                p.getResponsavel(),
                                p.getStatus()))
                        .toList();

        return new ReuniaoDetalheDTO(
                reuniao.getId(),
                reuniao.getCliente().getNome(),
                reuniao.getTitulo(),
                reuniao.getData(),
                reuniao.getStatus(),
                reuniao.getSentimento(),
                reuniao.getResumo(),
                reuniao.getPontoAtencao(),
                reuniao.getTranscricao(),
                oportunidades,
                riscos,
                pendencias
        );
    }
}