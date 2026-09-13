package org.example.service;

import org.example.dao.PendenciaDAO;
import org.example.model.Pendencia;

import java.util.List;

public class PendenciaService {
    private PendenciaDAO pendenciaDAO;
    List<Pendencia> pendencias;

    public PendenciaService() {
        this.pendenciaDAO = new PendenciaDAO();
        this.pendencias = pendenciaDAO.buscarTodos();
    }

    public Pendencia buscarPorId(int id) {
        return pendenciaDAO.buscarPorId(id);
    }

    public void listarPendencias() {

        pendencias = pendenciaDAO.buscarTodos();

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

        pendenciaDAO.atualizar(pendencia);

        System.out.println("Pendência concluida com sucesso!");
    }
}
