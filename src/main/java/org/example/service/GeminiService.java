package org.example.service;

import org.example.config.Config;
import org.example.model.ResultadoAnaliseIA;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class GeminiService {

    public ResultadoAnaliseIA analisarTranscricao(String transcricao) {

        try {

            String apiKey = Config.get("gemini.api.key");

            String prompt = """
                    Você é um analista de reuniões comerciais da TOTVS.
                    
                    Analise a transcrição enviada.
                    
                    Retorne SOMENTE um JSON válido, sem markdown, sem explicações e sem texto adicional.
                    
                    Formato:
                    
                    {
                      "resumo":"...",
                      "sentimento":"POSITIVO",
                      "pontosAtencao":"...",
                      "pendencias":[
                        "pendencia 1",
                        "pendencia 2"
                      ]
                    }
                    
                    Regras:
                    
                    - O resumo deve ser curto e objetivo.
                    - O sentimento deve ser apenas POSITIVO, NEUTRO ou NEGATIVO.
                    - Os pontos de atenção devem destacar riscos, dúvidas ou oportunidades.
                    - Se não houver pendências, retornar lista vazia.
                    - Não invente informações.
                    
                    Transcrição:
                    
                    """ + transcricao;

            JSONObject body = new JSONObject();

            JSONObject part = new JSONObject();
            part.put("text", prompt);

            JSONArray parts = new JSONArray();
            parts.put(part);

            JSONObject content = new JSONObject();
            content.put("parts", parts);

            JSONArray contents = new JSONArray();
            contents.put(content);

            body.put("contents", contents);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(
                            URI.create(
                                    "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?key="
                                            + apiKey
                            )
                    )
                    .header("Content-Type", "application/json")
                    .POST(
                            HttpRequest.BodyPublishers.ofString(
                                    body.toString()
                            )
                    )
                    .build();

            HttpClient client = HttpClient.newHttpClient();

            HttpResponse<String> response =
                    client.send(
                            request,
                            HttpResponse.BodyHandlers.ofString()
                    );

            JSONObject resposta =
                    new JSONObject(response.body());

            String texto =
                    resposta
                            .getJSONArray("candidates")
                            .getJSONObject(0)
                            .getJSONObject("content")
                            .getJSONArray("parts")
                            .getJSONObject(0)
                            .getString("text");

            return converterResposta(texto);

        } catch (Exception e) {

            e.printStackTrace();

            return new ResultadoAnaliseIA(
                    "Erro ao analisar reunião.",
                    "NEUTRO",
                    "Não foi possível analisar a reunião.",
                    new ArrayList<>()
            );
        }
    }

    private ResultadoAnaliseIA converterResposta(String texto) {

        JSONObject json = new JSONObject(texto);

        List<String> pendencias = new ArrayList<>();

        JSONArray pendenciasJson =
                json.getJSONArray("pendencias");

        for (int i = 0; i < pendenciasJson.length(); i++) {
            pendencias.add(
                    pendenciasJson.getString(i)
            );
        }

        return new ResultadoAnaliseIA(
                json.getString("resumo"),
                json.getString("sentimento"),
                json.getString("pontosAtencao"),
                pendencias
        );
    }
}