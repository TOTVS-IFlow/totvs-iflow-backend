package org.example.service;

import org.example.model.HistoricoResolucao;
import org.example.model.Pendencia;

import java.util.ArrayList;
import java.util.List;

public class PendenciaService {
    private List<Pendencia> pendencias = new ArrayList<>();

    public List<Pendencia> getPendencias() {
        return pendencias;
    }

    public Pendencia buscarPorId(int id) {
        for (Pendencia pendencia: pendencias){
            if (pendencia.getId() == id){
                return pendencia;
            }
        }

        return null;
    }

    public void listarPendencias() {

        if (pendencias.isEmpty()) {
            System.out.println("Nenhuma pendência encontrada!");
            return;
        }

        for (Pendencia pendencia : pendencias) {
            pendencia.exibir();
        }
    }

    public void adicionarPendencia(Pendencia pendencia) {
        pendencias.add(pendencia);
    }

    public void concluirPendencia(int id, HistoricoResolucao historico) {
        for  (Pendencia pendencia : pendencias) {
            if (pendencia.getId() == id) {
                pendencia.concluir(historico);
                System.out.println("Pendência concluida com sucesso!");
                return;
            }
        }
        System.out.println("Pendência não encontrada.");
    }
}
