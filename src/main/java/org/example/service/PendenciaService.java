package org.example.service;

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
            System.out.println("ID: " + pendencia.getId());
            System.out.println("Descrição: " + pendencia.getDescricao());
            System.out.println("Responsável: " + pendencia.getResponsavel());
            System.out.println("Status: " + pendencia.getStatus());
            System.out.println("----------------------------");
        }
    }

    public void adicionarPendencia(Pendencia pendencia) {
        pendencias.add(pendencia);
    }

    public void concluirPendencia(int id) {
        Pendencia pendencia = buscarPorId(id);

        if (pendencia == null) {
            System.out.println("Pendência não encontrada.");
            return;
        }

        pendencia.concluir();
        System.out.println("Pendência concluida com sucesso!");
    }
}
