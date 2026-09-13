package org.example.dao;

import org.example.config.ConnectionFactory;
import org.example.model.Cliente;
import org.example.model.Reuniao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReuniaoDAO {

    public void salvar(Reuniao reuniao) {

        String sql = """
                INSERT INTO meetings
                (client_id, title, meeting_date, status, sentiment, summary, attention_point, transcript)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, new String[]{"id"})) {

            statement.setInt(1, reuniao.getCliente().getId());
            statement.setString(2, reuniao.getTitulo());
            statement.setTimestamp(3, Timestamp.valueOf(reuniao.getData()));
            statement.setString(4, reuniao.getStatus());
            statement.setString(5, reuniao.getSentimento());
            statement.setString(6, reuniao.getResumo());
            statement.setString(7, reuniao.getPontoAtencao());
            statement.setString(8, reuniao.getTranscricao());

            statement.executeUpdate();

            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    reuniao.setId(keys.getInt(1));
                }
            }

            System.out.println("Reunião salva com sucesso!");

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar reunião.", e);
        }
    }

    public Reuniao buscarPorId(int id) {

        String sql = """
                SELECT m.*, c.name, c.sector, c.product
                FROM meetings m
                JOIN clients c ON c.id = m.client_id
                WHERE m.id = ?
                
                """;

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            ResultSet result = statement.executeQuery();

            if (result.next()) {
                return mapearReuniao(result);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar reunião.", e);
        }

        return null;
    }

    public List<Reuniao> buscarTodos() {

        String sql = """
                SELECT m.*, c.name, c.sector, c.product
                FROM meetings m
                JOIN clients c ON c.id = m.client_id
                ORDER BY m.id
                """;

        List<Reuniao> reunioes = new ArrayList<>();

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet result = statement.executeQuery()) {

            while (result.next()) {
                reunioes.add(mapearReuniao(result));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar reuniões.", e);
        }

        return reunioes;
    }

    public void atualizar(Reuniao reuniao) {

        String sql = """
                UPDATE meetings
                SET client_id = ?,
                    title = ?,
                    meeting_date = ?,
                    status = ?,
                    sentiment = ?,
                    summary = ?,
                    attention_point = ?,
                    transcript = ?
                WHERE id = ?
                """;

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, reuniao.getCliente().getId());
            statement.setString(2, reuniao.getTitulo());
            statement.setTimestamp(3, Timestamp.valueOf(reuniao.getData()));
            statement.setString(4, reuniao.getStatus());
            statement.setString(5, reuniao.getSentimento());
            statement.setString(6, reuniao.getResumo());
            statement.setString(7, reuniao.getPontoAtencao());
            statement.setString(8, reuniao.getTranscricao());
            statement.setInt(9, reuniao.getId());

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar reunião.", e);
        }
    }

    public void deletar(int id) {

        String sql = "DELETE FROM meetings WHERE id = ?";

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar reunião.", e);
        }
    }

    private Reuniao mapearReuniao(ResultSet result) throws SQLException {

        Cliente cliente = new Cliente(
                result.getInt("client_id"),
                result.getString("name"),
                result.getString("sector"),
                result.getString("product")
        );

        return new Reuniao(
                result.getInt("id"),
                cliente,
                result.getString("title"),
                result.getTimestamp("meeting_date").toLocalDateTime(),
                result.getString("status"),
                result.getString("sentiment"),
                result.getString("summary"),
                result.getString("attention_point"),
                result.getString("transcript")
        );
    }
}