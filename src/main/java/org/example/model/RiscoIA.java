package org.example.model;

public class RiscoIA {

    private String nivel;
    private String descricao;

    public RiscoIA(String nivel, String descricao) {
        this.nivel = nivel;
        this.descricao = descricao;
    }

    public String getNivel() {
        return nivel;
    }

    public String getDescricao() {
        return descricao;
    }
}