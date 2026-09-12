package org.example.dao;

import org.example.config.ConnectionFactory;
import org.example.model.Risco;
import org.example.model.Reuniao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RiscoDAO {

    private ReuniaoDAO reuniaoDAO = new ReuniaoDAO();

    public void salvar(Risco risco) {

        String sql = """
                INSERT INTO risks
                (meeting_id, risk_level, description)
                VALUES (?, ?, ?)
                """;

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, risco.getReuniao().getId());
            statement.setString(2, risco.getNivel());
            statement.setString(3, risco.getDescricao());

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar risco.", e);
        }
    }

    public Risco buscarPorId(int id) {

        String sql = """
                SELECT *
                FROM risks
                WHERE id = ?
                """;

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            ResultSet result = statement.executeQuery();

            if (result.next()) {
                return mapearRisco(result);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar risco.", e);
        }

        return null;
    }

    public List<Risco> buscarTodos() {

        String sql = "SELECT * FROM risks ORDER BY id ASC";

        List<Risco> riscos = new ArrayList<>();

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet result = statement.executeQuery()) {

            while (result.next()) {
                riscos.add(mapearRisco(result));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar riscos.", e);
        }

        return riscos;
    }

    public void atualizar(Risco risco) {

        String sql = """
                UPDATE risks
                SET meeting_id = ?,
                    risk_level = ?,
                    description = ?
                WHERE id = ?
                """;

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, risco.getReuniao().getId());
            statement.setString(2, risco.getNivel());
            statement.setString(3, risco.getDescricao());
            statement.setInt(4, risco.getId());

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar risco.", e);
        }
    }

    public void deletar(int id) {

        String sql = "DELETE FROM risks WHERE id = ?";

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar risco.", e);
        }
    }

    private Risco mapearRisco(ResultSet result) throws SQLException {

        Reuniao reuniao = reuniaoDAO.buscarPorId(
                result.getInt("meeting_id")
        );

        return new Risco(
                result.getInt("id"),
                reuniao,
                result.getString("level"),
                result.getString("description")
        );
    }
}