package org.example.dao;

import org.example.config.ConnectionFactory;
import org.example.model.Pendencia;
import org.example.model.Reuniao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PendenciaDAO {

    private ReuniaoDAO reuniaoDAO = new ReuniaoDAO();

    public void salvar(Pendencia pendencia) {

        String sql = """
                INSERT INTO pending_items
                (meeting_id, description, owner, status, closed_at)
                VALUES (?, ?, ?, ?, ?)
                """;

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement =  connection.prepareStatement(sql, new String[]{"ID"})) {

            statement.setInt(1, pendencia.getReuniao().getId());
            statement.setString(2, pendencia.getDescricao());
            statement.setString(3, pendencia.getResponsavel());
            statement.setString(4, pendencia.getStatus());

            if (pendencia.getDataConclusao() != null) {
                statement.setTimestamp(
                        5,
                        Timestamp.valueOf(pendencia.getDataConclusao())
                );
            } else {
                statement.setNull(5, Types.TIMESTAMP);
            }

            statement.executeUpdate();

            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    pendencia.setId(keys.getInt(1));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar pendência.", e);
        }
    }

    public Pendencia buscarPorId(int id) {

        String sql = """
                SELECT *
                FROM pending_items
                WHERE id = ?
                """;

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            ResultSet result = statement.executeQuery();

            if (result.next()) {
                return mapearPendencia(result);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar pendência.", e);
        }

        return null;
    }

    public List<Pendencia> buscarTodos() {

        String sql = "SELECT * FROM pending_items ORDER BY id ASC";

        List<Pendencia> pendencias = new ArrayList<>();

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet result = statement.executeQuery()) {

            while (result.next()) {
                pendencias.add(mapearPendencia(result));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar pendências.", e);
        }

        return pendencias;
    }

    public void atualizar(Pendencia pendencia) {

        String sql = """
                UPDATE pending_items
                SET meeting_id = ?,
                    description = ?,
                    owner = ?,
                    status = ?,
                    closed_at = ?
                WHERE id = ?
                """;

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, pendencia.getReuniao().getId());
            statement.setString(2, pendencia.getDescricao());
            statement.setString(3, pendencia.getResponsavel());
            statement.setString(4, pendencia.getStatus());

            if (pendencia.getDataConclusao() != null) {
                statement.setTimestamp(
                        5,
                        Timestamp.valueOf(pendencia.getDataConclusao())
                );
            } else {
                statement.setNull(5, Types.TIMESTAMP);
            }

            statement.setInt(6, pendencia.getId());

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar pendência.", e);
        }
    }

    public void deletar(int id) {

        String sql = "DELETE FROM pending_items WHERE id = ?";

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar pendência.", e);
        }
    }

    private Pendencia mapearPendencia(ResultSet result) throws SQLException {

        Reuniao reuniao = reuniaoDAO.buscarPorId(
                result.getInt("meeting_id")
        );

        Pendencia pendencia = new Pendencia(
                result.getInt("id"),
                reuniao,
                result.getString("description"),
                result.getString("owner"),
                result.getString("status")
        );

        Timestamp closedAt = result.getTimestamp("closed_at");

        if (closedAt != null) {
            pendencia.setDataConclusao(closedAt.toLocalDateTime());
        }

        return pendencia;
    }
}