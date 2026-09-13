package org.example.dao;

import org.example.config.ConnectionFactory;
import org.example.model.Cliente;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO {

    public void salvar(Cliente cliente) {

        String sql = """
                INSERT INTO clients (name, sector, product)
                VALUES (?, ?, ?)
                """;

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, cliente.getNome());
            statement.setString(2, cliente.getSetor());
            statement.setString(3, cliente.getProduto());

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar cliente.", e);
        }
    }

    public Cliente buscarPorId(int id) {

        String sql = "SELECT * FROM clients WHERE id = ?";

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            ResultSet result = statement.executeQuery();

            if (result.next()) {
                return mapearCliente(result);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar cliente.", e);
        }

        return null;
    }

    public List<Cliente> buscarTodos() {

        String sql = "SELECT * FROM clients ORDER BY id ASC";

        List<Cliente> clientes = new ArrayList<>();

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet result = statement.executeQuery()) {

            while (result.next()) {
                clientes.add(mapearCliente(result));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar clientes.", e);
        }

        return clientes;
    }

    public void atualizar(Cliente cliente) {

        String sql = """
                UPDATE clients
                SET name = ?, sector = ?, product = ?
                WHERE id = ?
                """;

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, cliente.getNome());
            statement.setString(2, cliente.getSetor());
            statement.setString(3, cliente.getProduto());
            statement.setInt(4, cliente.getId());

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar cliente.", e);
        }
    }

    public void deletar(int id) {

        String sql = "DELETE FROM clients WHERE id = ?";

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar cliente.", e);
        }
    }

    private Cliente mapearCliente(ResultSet result) throws SQLException {

        return new Cliente(
                result.getInt("id"),
                result.getString("name"),
                result.getString("sector"),
                result.getString("product")
        );
    }
}