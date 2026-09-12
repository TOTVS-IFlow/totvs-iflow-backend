package org.example.view;

import org.example.dao.ClienteDAO;
import org.example.model.Cliente;
import org.example.model.Pendencia;
import org.example.service.PendenciaService;
import org.example.service.ReuniaoService;

import java.util.List;
import java.util.Scanner;

public class MenuConsole {
    private Scanner scanner = new Scanner(System.in);

    public void iniciar() {
        int opcao;
        PendenciaService pendenciaService = new PendenciaService();
        ReuniaoService reuniaoService = new ReuniaoService(pendenciaService);

        ClienteDAO clienteDAO = new ClienteDAO();

        do {
            System.out.println("\n===== TOTVS IFLOW =====");
            System.out.println("1 - Analisar reunião");
            System.out.println("2 - Listar reuniões");
            System.out.println("3 - Ver detalhes de uma reunião");
            System.out.println("4 - Visualizar pendências");
            System.out.println("5 - Concluir pendência");
            System.out.println("6 - Dashboard / Resumo");
            System.out.println("7 - Cadastrar cliente");
            System.out.println("8 - Listar clientes");
            System.out.println("9 - Sair");
            System.out.print("Escolha uma opção: ");

            opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {

                case 1:

                    System.out.println("\n===== SELECIONAR CLIENTE =====");

                    List<Cliente> clientes = clienteDAO.buscarTodos();

                    if (clientes.isEmpty()) {
                        System.out.println("Nenhum cliente cadastrado.");
                        break;
                    }

                    for (Cliente cliente : clientes) {
                        System.out.println(
                                "ID: " + cliente.getId()
                                        + " | Nome: " + cliente.getNome()
                        );
                    }

                    System.out.print("Informe o ID do cliente: ");
                    int clientId = scanner.nextInt();
                    scanner.nextLine();

                    Cliente clienteSelecionado = clienteDAO.buscarPorId(clientId);

                    if (clienteSelecionado == null) {
                        System.out.println("Cliente não encontrado.");
                        break;
                    }

                    System.out.println("\nCliente selecionado: "
                            + clienteSelecionado.getNome());

                    System.out.println("\nDigite a transcrição.");
                    System.out.println("Digite FIM para encerrar.");

                    StringBuilder transcricao = new StringBuilder();

                    while (true) {

                        String linha = scanner.nextLine();

                        if (linha.equalsIgnoreCase("FIM")) {
                            break;
                        }

                        transcricao.append(linha).append("\n");
                    }

                    reuniaoService.analisarReuniao(
                            transcricao.toString(),
                            clientId
                    );

                    break;

                case 2:

                    reuniaoService.listarReunioes();

                    break;

                case 3:

                    System.out.print("Informe o ID da reunião: ");

                    int idReuniao = scanner.nextInt();
                    scanner.nextLine();

                    reuniaoService.exibirDetalhesReuniao(idReuniao);

                    break;

                case 4:

                    pendenciaService.listarPendencias();

                    break;

                case 5:

                    System.out.print(
                            "Informe o ID da pendência que deseja concluir: "
                    );

                    int idPendencia = scanner.nextInt();
                    scanner.nextLine();

                    pendenciaService.concluirPendencia(idPendencia);

                    break;

                case 6:

                    reuniaoService.exibirDashboard();

                    break;

                case 7:

                    cadastrarCliente(clienteDAO);

                    break;

                case 8:

                    listarClientes(clienteDAO);

                    break;

                case 9:

                    System.out.println("Encerrando o sistema...");

                    break;

                default:

                    System.out.println("Opção inválida.");
            }
        } while (opcao != 9);
    }

    private void cadastrarCliente(ClienteDAO clienteDAO) {

        System.out.println("\n===== CADASTRAR CLIENTE =====");

        System.out.print("Nome: ");
        String nome = scanner.nextLine();

        System.out.print("Setor: ");
        String setor = scanner.nextLine();

        System.out.print("Produto: ");
        String produto = scanner.nextLine();

        Cliente cliente = new Cliente(
                0,
                nome,
                setor,
                produto
        );

        clienteDAO.salvar(cliente);

        System.out.println("Cliente cadastrado com sucesso!");
    }

    private void listarClientes(ClienteDAO clienteDAO) {

        List<Cliente> clientes = clienteDAO.buscarTodos();

        System.out.println("\n===== CLIENTES =====");

        if (clientes.isEmpty()) {
            System.out.println("Nenhum cliente cadastrado.");
            return;
        }

        for (Cliente cliente : clientes) {
            System.out.println(
                    "ID: " + cliente.getId()
                            + " | Nome: " + cliente.getNome()
                            + " | Setor: " + cliente.getSetor()
                            + " | Produto: " + cliente.getProduto()
            );
        }
    }
}
