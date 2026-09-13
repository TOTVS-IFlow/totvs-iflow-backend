package org.example;

import org.example.model.Cliente;
import org.example.model.Pendencia;
import org.example.model.Risco;
import org.example.model.Reuniao;

import java.time.LocalDateTime;

public class TesteRegraNegocio {

    public static void main(String[] args) {

        // Criação do cliente
        Cliente cliente = new Cliente(
                1,
                "Empresa Teste",
                "Varejo",
                "IFlow"
        );

        // Criação da reunião
        Reuniao reuniao = new Reuniao(
                1,
                cliente,
                "Reunião de teste",
                LocalDateTime.now(),
                "analyzed",
                "negative",
                "Cliente demonstrou insatisfação com o serviço.",
                "Necessidade de melhoria no atendimento.",
                "Transcrição de teste da reunião."
        );

        // Criação dos riscos
        Risco risco1 = new Risco(
                1,
                reuniao,
                "high",
                "Risco de perda do cliente."
        );

        Risco risco2 = new Risco(
                2,
                reuniao,
                "medium",
                "Risco de atraso na solução."
        );

        reuniao.adicionarRisco(risco1);
        reuniao.adicionarRisco(risco2);

        // Criação das pendências
        Pendencia pendencia1 = new Pendencia(
                1,
                reuniao,
                "Entrar em contato com o cliente.",
                "Responsável 1",
                "open"
        );

        Pendencia pendencia2 = new Pendencia(
                2,
                reuniao,
                "Enviar proposta atualizada.",
                "Responsável 2",
                "open"
        );

        Pendencia pendencia3 = new Pendencia(
                3,
                reuniao,
                "Agendar nova reunião.",
                "Responsável 3",
                "open"
        );

        reuniao.adicionarPendencia(pendencia1);
        reuniao.adicionarPendencia(pendencia2);
        reuniao.adicionarPendencia(pendencia3);

        System.out.println("===== TESTE DAS REGRAS DE NEGÓCIO =====");

        // 1. Teste do método concluir()
        System.out.println("\n1. Conclusão de pendência:");

        pendencia1.concluir();

        System.out.println("Status: " + pendencia1.getStatus());
        System.out.println("Data de conclusão: " + pendencia1.getDataConclusao());

        // 2. Teste do cálculo do nível de risco
        System.out.println("\n2. Cálculo do nível de risco:");

        String nivelRisco = reuniao.calcularNivelRisco();

        System.out.println("Nível de risco: " + nivelRisco);

        // 3. Teste do percentual de pendências concluídas
        System.out.println("\n3. Percentual de pendências concluídas:");

        double percentual = reuniao.calcularPercentualPendenciasConcluidas();

        System.out.println("Percentual concluído: " + percentual + "%");

        // 4. Teste do cálculo da prioridade
        System.out.println("\n4. Cálculo da prioridade:");

        String prioridade = reuniao.calcularPrioridade();

        System.out.println("Prioridade: " + prioridade);

        System.out.println("\n===== FIM DOS TESTES =====");
    }
}