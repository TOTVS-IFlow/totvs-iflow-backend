package org.example.dto;

public class SentimentoDTO {

    private String sentiment;
    private int value;

    public SentimentoDTO(String sentiment, int value) {
        this.sentiment = sentiment;
        this.value = value;
    }

    public String getSentiment() {
        return sentiment;
    }

    public int getValue() {
        return value;
    }
}