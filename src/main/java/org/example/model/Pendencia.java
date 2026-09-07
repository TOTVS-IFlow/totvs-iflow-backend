package org.example.model;

import java.time.LocalDateTime;

public class Pendencia {
    private int id;
    private Reuniao reuniao;
    private String descricao;
    private String responsavel;
    private String status;
    private LocalDateTime dataConclusao;

    public Pendencia(int id, Reuniao reuniao, String descricao, String responsavel, String status) {
        this.id = id;
        this.reuniao = reuniao;
        this.descricao = descricao;
        this.responsavel = responsavel;
        this.status = status;
    }

//    Getters

    public int getId() {
        return id;
    }

    public Reuniao getReuniao() {
        return reuniao;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getResponsavel() {
        return responsavel;
    }

    public String getStatus() {
        return status;
    }

    public LocalDateTime getDataConclusao() {
        return dataConclusao;
    }

//    Setters

    public void setId(int id) {
        this.id = id;
    }

    public void setReuniao(Reuniao reuniao) {
        this.reuniao = reuniao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public void setResponsavel(String responsavel) {
        this.responsavel = responsavel;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setDataConclusao(LocalDateTime dataConclusao) {
        this.dataConclusao = dataConclusao;
    }

    public void concluir(){
        this.status = "done";
        this.dataConclusao = LocalDateTime.now();
    }
}
