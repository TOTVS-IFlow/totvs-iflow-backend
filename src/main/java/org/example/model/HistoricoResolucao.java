package org.example.model;

import java.time.LocalDateTime;

public class HistoricoResolucao {
    private int id;
    private String descricao;
    private LocalDateTime dataResolucao;

    public HistoricoResolucao(int id, String descricao, LocalDateTime dataResolucao) {
        this.id = id;
        this.descricao = descricao;
        this.dataResolucao = dataResolucao;
    }

    // Getters

    public int getId() {
        return id;
    }

    public String getDescricao() {
        return descricao;
    }

    public LocalDateTime getDataResolucao() {
        return dataResolucao;
    }

    // Setters

    public void setId(int id) {
        this.id = id;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public void setDataResolucao(LocalDateTime dataResolucao) {
        this.dataResolucao = dataResolucao;
    }

    public void exibirHistoricoResolucao(){
        System.out.println("Data da Resolução: " + this.dataResolucao);
        System.out.println("Descrição: " + this.descricao);
    }
}
