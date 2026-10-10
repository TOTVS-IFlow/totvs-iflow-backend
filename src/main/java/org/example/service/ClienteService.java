package org.example.service;

import org.example.dao.ClienteDAO;
import org.example.model.Cliente;
import org.example.dto.ClienteResumoDTO;

import java.util.List;

public class ClienteService {

    private final ClienteDAO clienteDAO = new ClienteDAO();

    public List<Cliente> listarTodos() {
        return clienteDAO.buscarTodos();
    }

    public List<ClienteResumoDTO> listarResumoParaApi() {
        return clienteDAO.buscarResumoParaApi();
    }
}