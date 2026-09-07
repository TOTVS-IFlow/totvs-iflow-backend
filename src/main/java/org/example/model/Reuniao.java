package org.example.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Reuniao {
    private int id;
    private Cliente cliente;
    private String titulo;
    private LocalDateTime data;
    private String status;
    private String sentimento;
    private String resumo;
    private String pontoAtencao;
    private String transcricao;

    private List<Oportunidade> oportunidades;
    private List<Risco> riscos;
    private List<Pendencia> pendencias;

    public Reuniao(int id, Cliente cliente, String titulo, LocalDateTime data, String status, String sentimento, String resumo, String pontoAtencao, String transcricao) {
        this.id = id;
        this.cliente = cliente;
        this.titulo = titulo;
        this.data = data;
        this.status = status;
        this.sentimento = sentimento;
        this.resumo = resumo;
        this.pontoAtencao = pontoAtencao;
        this.transcricao = transcricao;

        this.oportunidades = new ArrayList<>();
        this.riscos = new ArrayList<>();
        this.pendencias = new ArrayList<>();
    }

//    Getters

    public int getId() {
        return id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public String getTitulo() {
        return titulo;
    }

    public LocalDateTime getData() {
        return data;
    }

    public String getStatus() {
        return status;
    }

    public String getSentimento() {
        return sentimento;
    }

    public String getResumo() {
        return resumo;
    }

    public String getPontoAtencao() {
        return pontoAtencao;
    }

    public String getTranscricao() {
        return transcricao;
    }

    public List<Oportunidade> getOportunidades() {
        return oportunidades;
    }

    public List<Risco> getRiscos() {
        return riscos;
    }

    public List<Pendencia> getPendencias() {
        return pendencias;
    }

//    Setters

    public void setId(int id) {
        this.id = id;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setData(LocalDateTime data) {
        this.data = data;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setSentimento(String sentimento) {
        this.sentimento = sentimento;
    }

    public void setResumo(String resumo) {
        this.resumo = resumo;
    }

    public void setPontoAtencao(String pontoAtencao) {
        this.pontoAtencao = pontoAtencao;
    }

    public void setTranscricao(String transcricao) {
        this.transcricao = transcricao;
    }

    public void adicionarOportunidade(Oportunidade oportunidade){
        oportunidades.add(oportunidade);
    }

    public void adicionarRisco(Risco risco){
        riscos.add(risco);
    }

    public void adicionarPendencia(Pendencia pendencia){
        pendencias.add(pendencia);
    }
}
