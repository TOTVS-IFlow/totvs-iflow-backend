package org.example.service;

import org.example.config.Config;
import org.example.model.OportunidadeIA;
import org.example.model.ResultadoAnaliseIA;
import org.example.model.RiscoIA;
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
                    
                    Formato obrigatório:
                    
                    {
                      "resumo": "...",
                      "sentimento": "positive",
                      "pontosAtencao": "...",
                      "pendencias": [
                        "pendencia 1",
                        "pendencia 2"
                      ],
                      "riscos": [
                        {
                          "nivel": "high",
                          "descricao": "Descrição do risco identificado"
                        }
                      ],
                      "oportunidades": [
                        {
                          "tag": "upsell",
                          "descricao": "Descrição da oportunidade identificada"
                        }
                      ]
                    }
                    
                    Regras obrigatórias:
                    
                    - "resumo" deve ser curto e objetivo.
                    
                    - "sentimento" DEVE ser exatamente um destes valores:
                      "positive", "neutral" ou "negative".
                    - Nunca utilize "POSITIVO", "NEUTRO", "NEGATIVO", "Positive", "Neutral" ou "Negative".
                    
                    - "pontosAtencao" deve destacar riscos, dúvidas ou oportunidades identificados na reunião.
                    
                    - "pendencias" deve conter somente pendências realmente identificadas na transcrição.
                    - Se não houver pendências, retorne uma lista vazia.
                    
                    - "riscos" deve conter somente riscos realmente identificados na transcrição.
                    - Cada risco deve possuir "nivel" e "descricao".
                    - "nivel" DEVE ser exatamente um destes valores:
                      "low", "medium" ou "high".
                    - Se não houver riscos, retorne uma lista vazia.
                    - Não invente riscos.
                    
                    - "oportunidades" deve conter somente oportunidades comerciais realmente identificadas na transcrição.
                    - Cada oportunidade deve possuir "tag" e "descricao".
                    - "tag" DEVE ser exatamente um destes valores:
                      "upsell", "crosssell", "expansion", "addon" ou "renewal".
                    - Se não houver oportunidades, retorne uma lista vazia.
                    - Não invente oportunidades.
                    
                    - Não invente informações.
                    - Respeite exatamente os nomes dos campos apresentados no JSON.
                    - Não adicione outros campos.
                    
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

            if (response.statusCode() < 200 || response.statusCode() >= 300) {
                throw new RuntimeException(
                        "Falha ao consultar o serviço de análise."
                );
            }

            JSONObject resposta = new JSONObject(response.body());

            if (!resposta.has("candidates")
                    || resposta.getJSONArray("candidates").isEmpty()) {
                throw new RuntimeException(
                        "O serviço de análise não retornou um resultado válido."
                );
            }

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
            throw new RuntimeException(
                    "Não foi possível concluir a análise da reunião.",
                    e
            );
        }
    }

    private ResultadoAnaliseIA converterResposta(String texto) {

        if (texto == null || texto.isBlank()) {
            throw new RuntimeException(
                    "O serviço de análise retornou uma resposta vazia."
            );
        }

        texto = texto.trim();

        if (texto.startsWith("```")) {
            texto = texto.replaceFirst("^```(?:json)?\\s*", "");
            texto = texto.replaceFirst("\\s*```$", "");
        }

        JSONObject json = new JSONObject(texto);

        validarResultado(json);

        List<String> pendencias = new ArrayList<>();

        JSONArray pendenciasJson =
                json.getJSONArray("pendencias");

        for (int i = 0; i < pendenciasJson.length(); i++) {
            pendencias.add(
                    pendenciasJson.getString(i)
            );
        }

        List<RiscoIA> riscos = new ArrayList<>();

        JSONArray riscosJson =
                json.getJSONArray("riscos");

        for (int i = 0; i < riscosJson.length(); i++) {

            JSONObject riscoJson =
                    riscosJson.getJSONObject(i);

            riscos.add(
                    new RiscoIA(
                            riscoJson.getString("nivel"),
                            riscoJson.getString("descricao")
                    )
            );
        }

        List<OportunidadeIA> oportunidades = new ArrayList<>();

        JSONArray oportunidadesJson =
                json.getJSONArray("oportunidades");

        for (int i = 0; i < oportunidadesJson.length(); i++) {

            JSONObject oportunidadeJson =
                    oportunidadesJson.getJSONObject(i);

            oportunidades.add(
                    new OportunidadeIA(
                            oportunidadeJson.getString("tag"),
                            oportunidadeJson.getString("descricao")
                    )
            );
        }

        return new ResultadoAnaliseIA(
                json.getString("resumo"),
                json.getString("sentimento"),
                json.getString("pontosAtencao"),
                pendencias,
                riscos,
                oportunidades
        );
    }

    private void validarResultado(JSONObject json) {

        String sentimento = json.getString("sentimento");

        if (!List.of("positive", "neutral", "negative")
                .contains(sentimento)) {
            throw new RuntimeException(
                    "O serviço de análise retornou um sentimento inválido."
            );
        }

        JSONArray riscos = json.getJSONArray("riscos");

        for (int i = 0; i < riscos.length(); i++) {
            String nivel = riscos.getJSONObject(i).getString("nivel");

            if (!List.of("low", "medium", "high").contains(nivel)) {
                throw new RuntimeException(
                        "O serviço de análise retornou um nível de risco inválido."
                );
            }
        }

        JSONArray oportunidades = json.getJSONArray("oportunidades");

        for (int i = 0; i < oportunidades.length(); i++) {
            String tag = oportunidades.getJSONObject(i).getString("tag");

            if (!List.of("upsell", "crosssell", "expansion", "addon", "renewal")
                    .contains(tag)) {
                throw new RuntimeException(
                        "O serviço de análise retornou uma oportunidade inválida."
                );
            }
        }
    }
}