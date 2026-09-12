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

    public void analisarReuniao(String transcricao) {

        System.out.println("Analisando reunião...");

        ResultadoAnaliseIA resultado =
                geminiService.analisarTranscricao(transcricao);

        Cliente cliente = clienteDAO.buscarPorId(1);

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