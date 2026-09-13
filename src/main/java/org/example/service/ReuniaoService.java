package org.example.service;

import org.example.dao.*;
import org.example.model.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ReuniaoService {

    private PendenciaService pendenciaService;
    private GeminiService geminiService;

    private ReuniaoDAO reuniaoDAO;
    private ClienteDAO clienteDAO;
    private PendenciaDAO pendenciaDAO;
    private RiscoDAO riscoDAO;
    private OportunidadeDAO oportunidadeDAO;

    public ReuniaoService(PendenciaService pendenciaService) {
        this.pendenciaService = pendenciaService;
        this.geminiService = new GeminiService();

        this.reuniaoDAO = new ReuniaoDAO();
        this.clienteDAO = new ClienteDAO();
        this.pendenciaDAO = new PendenciaDAO();
        this.riscoDAO = new RiscoDAO();
        this.oportunidadeDAO = new OportunidadeDAO();
    }

    public void listarReunioes() {

        List<Reuniao> reunioes = reuniaoDAO.buscarTodos();

        if (reunioes.isEmpty()) {
            System.out.println("Nenhuma reunião cadastrada.");
            return;
        }

        for (Reuniao reuniao : reunioes) {
            System.out.println("ID: " + reuniao.getId());
            System.out.println("Título: " + reuniao.getTitulo());
            System.out.println("Data: " + reuniao.getData());
            System.out.println("Status: " + reuniao.getStatus());
            System.out.println("Sentimento: " + reuniao.getSentimento());
            System.out.println("----------------------------");
        }
    }

    public void exibirDetalhesReuniao(int idReuniao) {

        Reuniao reuniao = reuniaoDAO.buscarPorId(idReuniao);

        if (reuniao == null) {
            System.out.println("Reunião não encontrada.");
            return;
        }

        List<Pendencia> pendencias = pendenciaDAO.buscarPorReuniaoId(idReuniao);
        List<Risco> riscos = riscoDAO.buscarPorReuniaoId(idReuniao);
        List<Oportunidade> oportunidades = oportunidadeDAO.buscarPorReuniaoId(idReuniao);

        for (Pendencia pendencia : pendencias) {
            reuniao.adicionarPendencia(pendencia);
        }

        for (Risco risco : riscos) {
            reuniao.adicionarRisco(risco);
        }

        for (Oportunidade oportunidade : oportunidades) {
            reuniao.adicionarOportunidade(oportunidade);
        }

        System.out.println("\n===== DETALHES DA REUNIÃO =====");
        System.out.println("ID: " + reuniao.getId());
        System.out.println("Cliente: " + reuniao.getCliente().getNome());
        System.out.println("Título: " + reuniao.getTitulo());
        System.out.println("Data: " + reuniao.getData());
        System.out.println("Status: " + reuniao.getStatus());
        System.out.println("Sentimento: " + reuniao.getSentimento());

        System.out.println("\n--- Resumo ---");
        System.out.println(reuniao.getResumo());

        System.out.println("\n--- Ponto de atenção ---");
        System.out.println(reuniao.getPontoAtencao());

        System.out.println("\n--- Riscos ---");
        if (riscos.isEmpty()) {
            System.out.println("Nenhum risco identificado.");
        } else {
            for (Risco risco : riscos) {
                System.out.println(
                        "- [" + risco.getNivel() + "] " + risco.getDescricao()
                );
            }
        }

        System.out.println("\n--- Oportunidades ---");
        if (oportunidades.isEmpty()) {
            System.out.println("Nenhuma oportunidade identificada.");
        } else {
            for (Oportunidade oportunidade : oportunidades) {
                System.out.println(
                        "- [" + oportunidade.getTag() + "] "
                                + oportunidade.getDescricao()
                );
            }
        }

        System.out.println("\n--- Pendências ---");
        if (pendencias.isEmpty()) {
            System.out.println("Nenhuma pendência identificada.");
        } else {
            for (Pendencia pendencia : pendencias) {
                System.out.println(
                        "- ID: " + pendencia.getId()
                                + " | " + pendencia.getDescricao()
                                + " | Responsável: " + pendencia.getResponsavel()
                                + " | Status: " + pendencia.getStatus()
                );
            }
        }

        System.out.println("\n--- Indicadores ---");
        System.out.println("Nível de risco: " + reuniao.calcularNivelRisco());
        System.out.printf(
                "Pendências concluídas: %.2f%%%n",
                reuniao.calcularPercentualPendenciasConcluidas()
        );
        System.out.println("Prioridade: " + reuniao.calcularPrioridade());

        System.out.println("===============================");
    }

    public void exibirDashboard() {

        List<Reuniao> reunioes = reuniaoDAO.buscarTodos();
        List<Pendencia> pendencias = pendenciaDAO.buscarTodos();
        List<Risco> riscos = riscoDAO.buscarTodos();
        List<Oportunidade> oportunidades = oportunidadeDAO.buscarTodos();

        int pendenciasAbertas = 0;
        int riscosAltos = 0;

        int sentimentosPositivos = 0;
        int sentimentosNeutros = 0;
        int sentimentosNegativos = 0;

        for (Pendencia pendencia : pendencias) {
            if (pendencia.getStatus() != null &&
                    pendencia.getStatus().equalsIgnoreCase("open")) {
                pendenciasAbertas++;
            }
        }

        for (Risco risco : riscos) {
            if (risco.getNivel() != null &&
                    risco.getNivel().equalsIgnoreCase("high")) {
                riscosAltos++;
            }
        }

        for (Reuniao reuniao : reunioes) {

            if (reuniao.getSentimento() == null) {
                continue;
            }

            if (reuniao.getSentimento().equalsIgnoreCase("positive")) {
                sentimentosPositivos++;
            } else if (reuniao.getSentimento().equalsIgnoreCase("neutral")) {
                sentimentosNeutros++;
            } else if (reuniao.getSentimento().equalsIgnoreCase("negative")) {
                sentimentosNegativos++;
            }
        }

        System.out.println("\n===== DASHBOARD =====");

        System.out.println("Total de reuniões: " + reunioes.size());
        System.out.println("Pendências abertas: " + pendenciasAbertas);
        System.out.println("Oportunidades: " + oportunidades.size());
        System.out.println("Riscos altos: " + riscosAltos);

        System.out.println("\n--- Sentimento das reuniões ---");
        System.out.println("Positivo: " + sentimentosPositivos);
        System.out.println("Neutro: " + sentimentosNeutros);
        System.out.println("Negativo: " + sentimentosNegativos);

        System.out.println("=====================");
    }

    public void analisarReuniao(String transcricao, int clientId) {

        System.out.println("Analisando reunião...");

        ResultadoAnaliseIA resultado =
                geminiService.analisarTranscricao(transcricao);

        Cliente cliente = clienteDAO.buscarPorId(clientId);

        if (cliente == null) {
            System.out.println("Cliente não encontrado.");
            return;
        }

        Reuniao reuniao = new Reuniao(
                0,
                cliente,
                "Análise de reunião",
                LocalDateTime.now(),
                "analyzed",
                resultado.getSentimento(),
                resultado.getResumo(),
                resultado.getPontosAtencao(),
                transcricao
        );

        reuniaoDAO.salvar(reuniao);
        System.out.println("ID da reunião após salvar: " + reuniao.getId());

//        Salva pendencias
        for (String descricaoPendencia : resultado.getPendencias()) {

            Pendencia pendencia = new Pendencia(
                    0,
                    reuniao,
                    descricaoPendencia,
                    null,
                    "open"
            );

            pendenciaDAO.salvar(pendencia);
            pendenciaService.adicionarPendencia(pendencia);

            reuniao.adicionarPendencia(pendencia);
        }

//        Salva riscos
        for (RiscoIA riscoIA : resultado.getRiscos()) {

            Risco risco = new Risco(
                    0,
                    reuniao,
                    riscoIA.getNivel(),
                    riscoIA.getDescricao()
            );

            riscoDAO.salvar(risco);
            reuniao.adicionarRisco(risco);
        }

//        Salva oportunidades
        for (OportunidadeIA oportunidadeIA : resultado.getOportunidades()) {

            Oportunidade oportunidade = new Oportunidade(
                    0,
                    reuniao,
                    oportunidadeIA.getTag(),
                    oportunidadeIA.getDescricao()
            );

            oportunidadeDAO.salvar(oportunidade);
            reuniao.adicionarOportunidade(oportunidade);
        }

        System.out.println();
        System.out.println("===== RESULTADO DA ANÁLISE =====");

        System.out.println("Resumo:");
        System.out.println(reuniao.getResumo());

        System.out.println("\nSentimento:");
        System.out.println(reuniao.getSentimento());

        System.out.println("\nPontos de Atenção:");
        System.out.println(reuniao.getPontoAtencao());

        System.out.println("\nPendências Geradas:");

        if (resultado.getPendencias().isEmpty()) {

            System.out.println("Nenhuma pendência encontrada.");

        } else {

            for (Pendencia pendencia : reuniao.getPendencias()) {
                System.out.println("- " + pendencia.getDescricao());
            }
        }

        System.out.println("\nRiscos Identificados:");

        if (resultado.getRiscos().isEmpty()) {
            System.out.println("Nenhum risco encontrado.");
        } else {
            for (var risco : resultado.getRiscos()) {
                System.out.println("- [" + risco.getNivel() + "] "
                        + risco.getDescricao());
            }
        }

        System.out.println("\nOportunidades Identificadas:");

        if (resultado.getOportunidades().isEmpty()) {
            System.out.println("Nenhuma oportunidade encontrada.");
        } else {
            for (var oportunidade : resultado.getOportunidades()) {
                System.out.println("- [" + oportunidade.getTag() + "] "
                        + oportunidade.getDescricao());
            }
        }
    }
}