package org.example.model;

public class Risco {

    private int id;
    private Reuniao reuniao;
    private String nivel;
    private String descricao;

    public Risco(
            int id,
            Reuniao reuniao,
            String nivel,
            String descricao
    ) {
        this.id = id;
        this.reuniao = reuniao;
        this.nivel = nivel;
        this.descricao = descricao;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Reuniao getReuniao() {
        return reuniao;
    }

    public void setReuniao(Reuniao reuniao) {
        this.reuniao = reuniao;
    }

    public String getNivel() {
        return nivel;
    }

    public void setNivel(String nivel) {
        this.nivel = nivel;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}