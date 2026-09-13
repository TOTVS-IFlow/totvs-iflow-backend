package org.example.dao;

import org.example.config.ConnectionFactory;
import org.example.model.Oportunidade;
import org.example.model.Reuniao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OportunidadeDAO {

    private ReuniaoDAO reuniaoDAO = new ReuniaoDAO();

    public void salvar(Oportunidade oportunidade) {

        String sql = """
                INSERT INTO opportunities
                (meeting_id, tag, description)
                VALUES (?, ?, ?)
                """;

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, oportunidade.getReuniao().getId());
            statement.setString(2, oportunidade.getTag());
            statement.setString(3, oportunidade.getDescricao());

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar oportunidade.", e);
        }
    }

    public Oportunidade buscarPorId(int id) {

        String sql = """
                SELECT *
                FROM opportunities
                WHERE id = ?
                """;

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            ResultSet result = statement.executeQuery();

            if (result.next()) {
                return mapearOportunidade(result);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar oportunidade.", e);
        }

        return null;
    }

    public List<Oportunidade> buscarTodos() {

        String sql = "SELECT * FROM opportunities ORDER BY id ASC";

        List<Oportunidade> oportunidades = new ArrayList<>();

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet result = statement.executeQuery()) {

            while (result.next()) {
                oportunidades.add(mapearOportunidade(result));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar oportunidades.", e);
        }

        return oportunidades;
    }

    public List<Oportunidade> buscarPorReuniaoId(int meetingId) {

        String sql = """
            SELECT *
            FROM opportunities
            WHERE meeting_id = ?
            ORDER BY id ASC
            """;

        List<Oportunidade> oportunidades = new ArrayList<>();

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, meetingId);

            ResultSet result = statement.executeQuery();

            while (result.next()) {
                oportunidades.add(mapearOportunidade(result));
            }

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Erro ao buscar oportunidades da reunião.",
                    e
            );
        }

        return oportunidades;
    }

    public void atualizar(Oportunidade oportunidade) {

        String sql = """
                UPDATE opportunities
                SET meeting_id = ?,
                    tag = ?,
                    description = ?
                WHERE id = ?
                """;

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, oportunidade.getReuniao().getId());
            statement.setString(2, oportunidade.getTag());
            statement.setString(3, oportunidade.getDescricao());
            statement.setInt(4, oportunidade.getId());

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar oportunidade.", e);
        }
    }

    public void deletar(int id) {

        String sql = "DELETE FROM opportunities WHERE id = ?";

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar oportunidade.", e);
        }
    }

    private Oportunidade mapearOportunidade(ResultSet result) throws SQLException {

        Reuniao reuniao = reuniaoDAO.buscarPorId(
                result.getInt("meeting_id")
        );

        return new Oportunidade(
                result.getInt("id"),
                reuniao,
                result.getString("tag"),
                result.getString("description")
        );
    }
}