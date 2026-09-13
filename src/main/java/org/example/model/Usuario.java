package org.example.model;

public class Usuario {
    private int id;
    private String nome;
    private String email;
    private String senhaCriptografada;
    private String cargo;

    public Usuario(int id, String nome, String email, String senhaCriptografada, String cargo) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.senhaCriptografada = senhaCriptografada;
        this.cargo = cargo;
    }

    //    Getters

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public String getSenhaCriptografada() {
        return senhaCriptografada;
    }

    public String getCargo() {
        return cargo;
    }

//    Setters

    public void setId(int id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setSenhaCriptografada(String senhaCriptografada) {
        this.senhaCriptografada = senhaCriptografada;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }
}
