
package org.example.service;

import org.example.config.ConnectionFactory;
import org.example.dao.ClienteDAO;
import org.example.dao.ReuniaoDAO;
import org.example.dao.PendenciaDAO;
import org.example.dao.RiscoDAO;
import org.example.dao.OportunidadeDAO;
import org.example.dto.CriarReuniaoDTO;
import org.example.exception.RecursoNaoEncontradoException;
import org.example.model.Cliente;
import org.example.model.Reuniao;
import org.example.model.Pendencia;
import org.example.model.Risco;
import org.example.model.Oportunidade;
import org.example.model.ResultadoAnaliseIA;
import org.example.model.RiscoIA;
import org.example.model.OportunidadeIA;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class CriarReuniaoApiService {

    private final ClienteDAO clienteDAO = new ClienteDAO();
    private final ReuniaoDAO reuniaoDAO = new ReuniaoDAO();
    private final PendenciaDAO pendenciaDAO = new PendenciaDAO();
    private final RiscoDAO riscoDAO = new RiscoDAO();
    private final OportunidadeDAO oportunidadeDAO = new OportunidadeDAO();
    private final GeminiService geminiService = new GeminiService();

    public int criar(CriarReuniaoDTO dados) {

        validarDados(dados);

        // Verifica se o cliente informado existe antes de chamar a IA.
        if (dados.getClientId() != null) {
            Cliente cliente = clienteDAO.buscarPorId(dados.getClientId());

            if (cliente == null) {
                throw new RecursoNaoEncontradoException(
                        "Cliente não encontrado."
                );
            }
        }

        // A análise ocorre antes das gravações no banco.
        ResultadoAnaliseIA resultado =
                geminiService.analisarTranscricao(dados.getTranscript());

        try (Connection connection = ConnectionFactory.getConnection()) {

            connection.setAutoCommit(false);

            try {
                Cliente cliente;

                if (dados.getClientId() != null) {

                    cliente = clienteDAO.buscarPorId(
                            connection,
                            dados.getClientId()
                    );

                    if (cliente == null) {
                        throw new RecursoNaoEncontradoException(
                                "Cliente não encontrado."
                        );
                    }

                } else {

                    cliente = clienteDAO.buscarPorNome(
                            connection,
                            dados.getClientName()
                    );

                    if (cliente == null) {
                        Cliente novoCliente = new Cliente(
                                0,
                                dados.getClientName().trim(),
                                null,
                                null
                        );

                        clienteDAO.salvar(connection, novoCliente);

                        cliente = clienteDAO.buscarPorNome(
                                connection,
                                dados.getClientName()
                        );

                        if (cliente == null) {
                            throw new RuntimeException(
                                    "Não foi possível recuperar o cliente cadastrado."
                            );
                        }
                    }
                }

                Reuniao reuniao = new Reuniao(
                        0,
                        cliente,
                        dados.getTitle().trim(),
                        LocalDateTime.now(),
                        "analyzed",
                        resultado.getSentimento(),
                        resultado.getResumo(),
                        resultado.getPontosAtencao(),
                        dados.getTranscript()
                );

                reuniaoDAO.salvar(connection, reuniao);

                for (String descricao : resultado.getPendencias()) {
                    Pendencia pendencia = new Pendencia(
                            0,
                            reuniao,
                            descricao,
                            null,
                            "open"
                    );

                    pendenciaDAO.salvar(connection, pendencia);
                }

                for (RiscoIA riscoIA : resultado.getRiscos()) {
                    Risco risco = new Risco(
                            0,
                            reuniao,
                            riscoIA.getNivel(),
                            riscoIA.getDescricao()
                    );

                    riscoDAO.salvar(connection, risco);
                }

                for (OportunidadeIA oportunidadeIA
                        : resultado.getOportunidades()) {

                    Oportunidade oportunidade = new Oportunidade(
                            0,
                            reuniao,
                            oportunidadeIA.getTag(),
                            oportunidadeIA.getDescricao()
                    );

                    oportunidadeDAO.salvar(connection, oportunidade);
                }

                // Confirma todas as gravações juntas.
                connection.commit();

                return reuniao.getId();

            } catch (Exception e) {

                // Se uma operação falhar, desfaz as gravações da transação.
                try {
                    connection.rollback();
                } catch (SQLException erroRollback) {
                    e.addSuppressed(erroRollback);
                }

                throw e;
            }

        } catch (RecursoNaoEncontradoException
                 | IllegalArgumentException e) {

            throw e;


        } catch (Exception e) {
            e.printStackTrace();

            throw new RuntimeException(
                    "Não foi possível cadastrar a reunião e seus dados.",
                    e
            );
        }
    }

    private void validarDados(CriarReuniaoDTO dados) {

        if (dados == null) {
            throw new IllegalArgumentException(
                    "O corpo da requisição é obrigatório."
            );
        }

        boolean temClientId = dados.getClientId() != null;
        boolean temClientName = dados.getClientName() != null
                && !dados.getClientName().isBlank();

        if (temClientId == temClientName) {
            throw new IllegalArgumentException(
                    "Informe clientId ou clientName, mas não ambos."
            );
        }

        if (temClientId && dados.getClientId() <= 0) {
            throw new IllegalArgumentException(
                    "clientId deve ser positivo."
            );
        }

        if (dados.getTitle() == null || dados.getTitle().isBlank()) {
            throw new IllegalArgumentException(
                    "O título é obrigatório."
            );
        }

        if (dados.getTranscript() == null
                || dados.getTranscript().isBlank()) {
            throw new IllegalArgumentException(
                    "A transcrição é obrigatória."
            );
        }
    }
}