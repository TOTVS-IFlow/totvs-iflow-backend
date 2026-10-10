package org.example.dao;

import org.example.config.ConnectionFactory;
import org.example.model.Cliente;
import org.example.dto.ClienteResumoDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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


    public Cliente buscarPorNome(String nome) {

        String sql = """
                SELECT *
                FROM clients
                WHERE LOWER(TRIM(name)) = LOWER(TRIM(?))
                """;

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, nome);

            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    return mapearCliente(result);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar cliente pelo nome.", e);
        }

        return null;
    }


    public List<ClienteResumoDTO> buscarResumoParaApi() {


        String sql = """
                SELECT
                    c.id,
                    c.name,
                    c.sector,
                    c.product,
                
                    CASE
                        WHEN NVL((
                            SELECT AVG(
                                CASE
                                    WHEN LOWER(m.sentiment) = 'positive' THEN 1
                                    WHEN LOWER(m.sentiment) = 'negative' THEN -1
                                    ELSE 0
                                END
                            )
                            FROM meetings m
                            WHERE m.client_id = c.id
                        ), 0) > 0.2 THEN 'positive'
                
                        WHEN NVL((
                            SELECT AVG(
                                CASE
                                    WHEN LOWER(m.sentiment) = 'positive' THEN 1
                                    WHEN LOWER(m.sentiment) = 'negative' THEN -1
                                    ELSE 0
                                END
                            )
                            FROM meetings m
                            WHERE m.client_id = c.id
                        ), 0) < -0.2 THEN 'negative'
                
                        ELSE 'neutral'
                    END AS sentiment,
                
                    (SELECT COUNT(*)
                     FROM meetings m
                     WHERE m.client_id = c.id) AS meeting_count,
                
                    (SELECT COUNT(*)
                     FROM pending_items p
                     JOIN meetings m ON m.id = p.meeting_id
                     WHERE m.client_id = c.id
                       AND LOWER(p.status) = 'open') AS open_pending_count,
                
                    (SELECT COUNT(*)
                     FROM risks r
                     JOIN meetings m ON m.id = r.meeting_id
                     WHERE m.client_id = c.id
                       AND LOWER(r.risk_level) = 'high') AS high_risk_count
                
                FROM clients c
                ORDER BY c.name
                """;

        List<ClienteResumoDTO> clientes = new ArrayList<>();

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet result = statement.executeQuery()) {

            while (result.next()) {
                clientes.add(new ClienteResumoDTO(
                        result.getInt("id"),
                        result.getString("name"),
                        result.getString("sector"),
                        result.getString("product"),
                        result.getString("sentiment"),
                        result.getInt("meeting_count"),
                        result.getInt("open_pending_count"),
                        result.getInt("high_risk_count")
                ));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar resumo dos clientes.", e);
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