package org.example.model;

public class Cliente {
    private int id;
    private String nome;
    private String setor;
    private String produto;

    public Cliente(int id, String nome, String setor, String produto) {
        this.id = id;
        this.nome = nome;
        this.setor = setor;
        this.produto = produto;
    }

//   Getters

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getSetor() {
        return setor;
    }

    public String getProduto() {
        return produto;
    }

//    Setters

    public void setSetor(String setor) {
        this.setor = setor;
    }

    public void exibirDados(){
        System.out.println("Cliente: " + nome);
        System.out.println("Setor: " + setor);
        System.out.println("Produto: " + produto);
    }
}
