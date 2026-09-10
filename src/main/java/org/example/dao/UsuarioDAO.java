package org.example.dao;

import com.sun.security.auth.UnixNumericUserPrincipal;
import org.example.config.ConnectionFactory;
import org.example.model.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {
    public void salvar(Usuario usuario) {
        String sql = """
                INSERT INTO users (name, email, password_hash, role)
                VALUES (?, ?, ?, ?)
                """;

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, usuario.getNome());
            statement.setString(2, usuario.getEmail());
            statement.setString(3, usuario.getSenhaCriptografada());
            statement.setString(4, usuario.getCargo());

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar usuario: " + e);
        }
    }

    public Usuario buscarPorId(int id) {
        String sql = "SELECT * FROM users WHERE id = ?";

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);

            ResultSet result = statement.executeQuery();

            if (result.next()) {
                return mapearUsuario(result);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar usuario: " + e);
        }

        return null;
    }

    public List<Usuario> buscarTodos() {
        String sql = "SELECT * FROM users";

        List<Usuario> usuarios = new ArrayList<>();

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet result = statement.executeQuery();

            while (result.next()) {
                usuarios.add(mapearUsuario(result));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar usuarios: " + e);
        }

        return usuarios;
    }

    public void atualizar(Usuario usuario) {
        String sql = """
                UPDATE users
                SET name = ?, email = ?, password_hash = ?, role = ?
                WHERE id = ?
                """;

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, usuario.getNome());
            statement.setString(2, usuario.getEmail());
            statement.setString(3, usuario.getSenhaCriptografada());
            statement.setString(4, usuario.getCargo());
            statement.setInt(5, usuario.getId());

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar usuario: " + e);
        }
    }

    public void deletar(int id) {

        String sql = "DELETE FROM users WHERE id = ?";

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar usuario.", e);
        }
    }

    private Usuario mapearUsuario(ResultSet result) throws SQLException {

        return new Usuario(
                result.getInt("id"),
                result.getString("name"),
                result.getString("email"),
                result.getString("password_hash"),
                result.getString("role")
        );
    }

}
