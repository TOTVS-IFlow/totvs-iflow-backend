package org.example.view;

import org.example.model.Pendencia;
import org.example.service.PendenciaService;
import org.example.service.ReuniaoService;

import java.util.Scanner;

public class MenuConsole {
    private Scanner scanner = new Scanner(System.in);

    public void iniciar() {
        int opcao;
        PendenciaService pendenciaService = new PendenciaService();
        ReuniaoService reuniaoService = new ReuniaoService(pendenciaService);

        do {
            System.out.println("\n===== TOTVS IFLOW =====");
            System.out.println("1 - Analisar reunião");
            System.out.println("2 - Visualizar pendências");
            System.out.println("3 - Concluir pendência");
            System.out.println("4 - Listar reuniões");
            System.out.println("5 - Sair");
            System.out.print("Escolha uma opção: ");

            opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {
                case 1:
                    System.out.println("Digite a transcrição.");
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
                            transcricao.toString()
                    );
                    break;
                case 2:
                    pendenciaService.listarPendencias();
                    break;
                case 3:
                    System.out.print("Informe o ID da pendência que deseja concluir: ");
                    int id = scanner.nextInt();
                    scanner.nextLine();

                    pendenciaService.concluirPendencia(id);

                    break;
                case 4:
                    reuniaoService.listarReunioes();
                    break;
                case 5:
                    System.out.println("Encerrando o sistema...");
                    break;
                default:
                    System.out.println("Opção inválida");
            }
        } while (opcao != 5);
    }
}
