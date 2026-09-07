package org.example.model;

public class Oportunidade {

    private int id;
    private Reuniao reuniao;
    private String tag;
    private String descricao;

    public Oportunidade(
            int id,
            Reuniao reuniao,
            String tag,
            String descricao
    ) {
        this.id = id;
        this.reuniao = reuniao;
        this.tag = tag;
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

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}