package org.example.model;

import java.util.List;

public class ResultadoAnaliseIA {
    private String resumo;
    private String sentimento;
    private String pontosAtencao;
    private List<String> pendencias;

    public ResultadoAnaliseIA(
            String resumo,
            String sentimento,
            String pontosAtencao,
            List<String> pendencias) {

        this.resumo = resumo;
        this.sentimento = sentimento;
        this.pontosAtencao = pontosAtencao;
        this.pendencias = pendencias;
    }

    public String getResumo() {
        return resumo;
    }

    public String getSentimento() {
        return sentimento;
    }

    public List<String> getPendencias() {
        return pendencias;
    }

    public String getPontosAtencao() {
        return pontosAtencao;
    }
}
