package org.example.service;

import org.example.model.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ReuniaoService {
    private PendenciaService pendenciaService;
    private GeminiService geminiService;
    private List<Reuniao> reunioes = new ArrayList<>();

    private int proximoIdReuniao = 1;
    private int proximoIdPendencia = 1;

    public ReuniaoService(PendenciaService pendenciaService) {
        this.pendenciaService = pendenciaService;
        this.geminiService = new GeminiService();
    }

    public void listarReunioes() {
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
        System.out.println("Análise recebida:");
        System.out.println(transcricao);

        ResultadoAnaliseIA resultado =
                geminiService.analisarTranscricao(transcricao);
        Analise analise = new Analise(proximoIdAnalise++, resultado.getResumo(), resultado.getSentimento(), LocalDateTime.now());

        Cliente cliente = new Cliente(
                1,
                "12345",
                "Cliente",
                false,
                "SP",
                "6201500",
                "São Paulo",
                "Varejo",
                "Grande Porte",
                LocalDate.now(),
                75
        );

        for (String descricaoPendencia : resultado.getPendencias()) {

            Pendencia pendencia = new Pendencia(
                    proximoIdPendencia++,
                    "ABERTA",
                    descricaoPendencia
            );

            analise.adicionarPendencia(pendencia);

            pendenciaService.adicionarPendencia(pendencia);
        }

        Reuniao reuniao = new Reuniao(proximoIdReuniao++, LocalDateTime.now(), "ÁUDIO", "COMPLETED", "01", LocalDateTime.now(), transcricao, true, cliente, analise);
        reunioes.add(reuniao);

        System.out.println();
        System.out.println("===== RESULTADO DA ANÁLISE =====");

        analise.exibirResumo();

        System.out.println("\nPontos de Atenção:");
        System.out.println(resultado.getPontosAtencao());

        System.out.println("\nPendências Geradas:");

        if (resultado.getPendencias().isEmpty()) {

            System.out.println("Nenhuma pendência encontrada.");

        } else {

            for (String pendencia : resultado.getPendencias()) {
                System.out.println("- " + pendencia);
            }
        }
    }
}
