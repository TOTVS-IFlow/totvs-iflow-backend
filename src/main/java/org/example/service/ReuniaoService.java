package org.example.service;

import org.example.model.Cliente;
import org.example.model.Pendencia;
import org.example.model.ResultadoAnaliseIA;
import org.example.model.Reuniao;

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

        System.out.println("Analisando reunião...");

        ResultadoAnaliseIA resultado =
                geminiService.analisarTranscricao(transcricao);

        Cliente cliente = new Cliente(
                1,
                "Cliente",
                "Varejo",
                "IFlow"
        );

        Reuniao reuniao = new Reuniao(
                proximoIdReuniao++,
                cliente,
                "Análise de reunião",
                LocalDateTime.now(),
                "analyzed",
                resultado.getSentimento(),
                resultado.getResumo(),
                resultado.getPontosAtencao(),
                transcricao
        );

        for (String descricaoPendencia : resultado.getPendencias()) {

            Pendencia pendencia = new Pendencia(
                    proximoIdPendencia++,
                    reuniao,
                    descricaoPendencia,
                    null,
                    "open"
            );

            reuniao.adicionarPendencia(pendencia);
            pendenciaService.adicionarPendencia(pendencia);
        }

        reunioes.add(reuniao);

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
    }
}