package org.example.service;

import org.example.dao.OportunidadeDAO;
import org.example.dao.PendenciaDAO;
import org.example.dao.ReuniaoDAO;
import org.example.dao.RiscoDAO;
import org.example.dto.DashboardResumoDTO;
import org.example.dto.ReunioesPorMesDTO;
import org.example.dto.SentimentoDTO;
import org.example.model.Oportunidade;
import org.example.model.Pendencia;
import org.example.model.Reuniao;
import org.example.model.Risco;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

public class DashboardService {

    private final ReuniaoDAO reuniaoDAO = new ReuniaoDAO();
    private final PendenciaDAO pendenciaDAO = new PendenciaDAO();
    private final RiscoDAO riscoDAO = new RiscoDAO();
    private final OportunidadeDAO oportunidadeDAO = new OportunidadeDAO();

    private final DateTimeFormatter formatoMes =
            DateTimeFormatter.ofPattern("yyyy-MM");

    public DashboardResumoDTO gerarResumo(Integer clientId) {

        List<Reuniao> reunioes;

        if (clientId == null) {
            reunioes = reuniaoDAO.buscarTodos();
        } else {
            reunioes = reuniaoDAO.buscarPorClienteId(clientId);
        }

        Set<Integer> idsReunioes = new HashSet<>();

        for (Reuniao reuniao : reunioes) {
            idsReunioes.add(reuniao.getId());
        }

        int pendenciasAbertas = 0;
        int riscosAltos = 0;
        int totalOportunidades = 0;

        int sentimentosPositivos = 0;
        int sentimentosNeutros = 0;
        int sentimentosNegativos = 0;

        TreeMap<String, Integer> reunioesPorMes = new TreeMap<>();

        for (Reuniao reuniao : reunioes) {

            if (reuniao.getData() != null) {
                String mes = reuniao.getData().format(formatoMes);

                reunioesPorMes.put(
                        mes,
                        reunioesPorMes.getOrDefault(mes, 0) + 1
                );
            }

            String sentimento = reuniao.getSentimento();

            if (sentimento == null) {
                continue;
            }

            if (sentimento.equalsIgnoreCase("positive")) {
                sentimentosPositivos++;
            } else if (sentimento.equalsIgnoreCase("neutral")) {
                sentimentosNeutros++;
            } else if (sentimento.equalsIgnoreCase("negative")) {
                sentimentosNegativos++;
            }
        }

        List<Pendencia> pendencias = pendenciaDAO.buscarTodos();

        for (Pendencia pendencia : pendencias) {

            if (pendencia.getReuniao() != null
                    && idsReunioes.contains(
                    pendencia.getReuniao().getId())
                    && "open".equalsIgnoreCase(pendencia.getStatus())) {

                pendenciasAbertas++;
            }
        }

        List<Risco> riscos = riscoDAO.buscarTodos();

        for (Risco risco : riscos) {

            if (risco.getReuniao() != null
                    && idsReunioes.contains(
                    risco.getReuniao().getId())
                    && "high".equalsIgnoreCase(risco.getNivel())) {

                riscosAltos++;
            }
        }

        List<Oportunidade> oportunidades =
                oportunidadeDAO.buscarTodos();

        for (Oportunidade oportunidade : oportunidades) {

            if (oportunidade.getReuniao() != null
                    && idsReunioes.contains(
                    oportunidade.getReuniao().getId())) {

                totalOportunidades++;
            }
        }

        List<ReunioesPorMesDTO> listaMensal = new ArrayList<>();

        for (var entrada : reunioesPorMes.entrySet()) {
            listaMensal.add(
                    new ReunioesPorMesDTO(
                            entrada.getKey(),
                            entrada.getValue()
                    )
            );
        }

        List<SentimentoDTO> distribuicaoSentimentos =
                new ArrayList<>();

        distribuicaoSentimentos.add(
                new SentimentoDTO("positive", sentimentosPositivos)
        );

        distribuicaoSentimentos.add(
                new SentimentoDTO("neutral", sentimentosNeutros)
        );

        distribuicaoSentimentos.add(
                new SentimentoDTO("negative", sentimentosNegativos)
        );

        return new DashboardResumoDTO(
                reunioes.size(),
                pendenciasAbertas,
                totalOportunidades,
                riscosAltos,
                listaMensal,
                distribuicaoSentimentos
        );
    }
}