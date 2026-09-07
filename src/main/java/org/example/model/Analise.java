package org.example.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Analise {
    private int id;
    private String resumo;
    private String sentimento;
    private LocalDateTime dataAnalise;
    private List<Pendencia> pendencias;

    public Analise(int id, String resumo, String sentimento, LocalDateTime dataAnalise) {
        this.id = id;
        this.resumo = resumo;
        this.sentimento = sentimento;
        this.dataAnalise = dataAnalise;
        this.pendencias = new ArrayList<>();
    }

    // Getters

    public int getId() {
        return id;
    }

    public String getResumo() {
        return resumo;
    }

    public String getSentimento() {
        return sentimento;
    }

    public LocalDateTime getDataAnalise() {
        return dataAnalise;
    }

    public List<Pendencia> getPendencias() {
        return pendencias;
    }

    // Setter

    public void setId(int id) {
        this.id = id;
    }

    public void setResumo(String resumo) {
        this.resumo = resumo;
    }

    public void setSentimento(String sentimento) {
        this.sentimento = sentimento;
    }

    public void setDataAnalise(LocalDateTime dataAnalise) {
        this.dataAnalise = dataAnalise;
    }

    public void setPendencias(List<Pendencia> pendencias) {
        this.pendencias = pendencias;
    }

    public void adicionarPendencia(Pendencia pendencia) {
        this.pendencias.add(pendencia);
    }

    public void exibirResumo() {
        System.out.println("\n===== ANÁLISE DA REUNIÃO =====");

        System.out.println("\nResumo:");
        System.out.println(resumo);

        System.out.println("\nSentimento:");
        System.out.println(sentimento);
    }
}
