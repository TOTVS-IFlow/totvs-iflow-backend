package org.example.model;


public abstract class ItemAnalise {

    protected String descricao;

    public ItemAnalise(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public void exibir(){
        System.out.println(descricao);
    }
}
