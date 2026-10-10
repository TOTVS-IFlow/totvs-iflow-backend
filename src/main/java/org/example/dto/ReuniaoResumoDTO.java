package org.example.dto;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class ReuniaoResumoDTO {

    private int id;
    private String client;
    private String title;
    private Instant date;
    private String status;
    private String sentiment;
    private int openPendingCount;

    public ReuniaoResumoDTO(
            int id,
            String client,
            String title,
            LocalDateTime date,
            String status,
            String sentiment,
            int openPendingCount) {
        this.id = id;
        this.client = client;
        this.title = title;
        this.date = date == null
                ? null
                : date.atZone(ZoneId.of("America/Sao_Paulo")).toInstant();
        this.status = status;
        this.sentiment = sentiment;
        this.openPendingCount = openPendingCount;
    }

    public int getId() {
        return id;
    }

    public String getClient() {
        return client;
    }

    public String getTitle() {
        return title;
    }

    public Instant getDate() {
        return date;
    }

    public String getStatus() {
        return status;
    }

    public String getSentiment() {
        return sentiment;
    }

    public int getOpenPendingCount() {
        return openPendingCount;
    }
}