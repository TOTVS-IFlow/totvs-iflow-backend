package org.example.model;

import java.util.List;

public class ResultadoAnaliseIA {

    private String resumo;
    private String sentimento;
    private String pontosAtencao;
    private List<String> pendencias;
    private List<RiscoIA> riscos;
    private List<OportunidadeIA> oportunidades;

    public ResultadoAnaliseIA(
            String resumo,
            String sentimento,
            String pontosAtencao,
            List<String> pendencias,
            List<RiscoIA> riscos,
            List<OportunidadeIA> oportunidades) {

        this.resumo = resumo;
        this.sentimento = sentimento;
        this.pontosAtencao = pontosAtencao;
        this.pendencias = pendencias;
        this.riscos = riscos;
        this.oportunidades = oportunidades;
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

    public List<RiscoIA> getRiscos() {
        return riscos;
    }

    public List<OportunidadeIA> getOportunidades() {
        return oportunidades;
    }
}