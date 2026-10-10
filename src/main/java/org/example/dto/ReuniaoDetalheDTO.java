package org.example.dto;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

public class ReuniaoDetalheDTO {
    private int id;
    private String client;
    private String title;
    private Instant date;
    private String status;
    private String sentiment;
    private String summary;
    private String attentionPoint;
    private String transcript;
    private List<OportunidadeDTO> opportunities;
    private List<RiscoDTO> risks;
    private List<PendenciaDTO> pendingItems;

    public ReuniaoDetalheDTO(
            int id, String client, String title, LocalDateTime date,
            String status, String sentiment, String summary,
            String attentionPoint, String transcript,
            List<OportunidadeDTO> opportunities,
            List<RiscoDTO> risks, List<PendenciaDTO> pendingItems) {
        this.id = id;
        this.client = client;
        this.title = title;
        this.date = date == null
                ? null
                : date.atZone(ZoneId.of("America/Sao_Paulo")).toInstant();
        this.status = status;
        this.sentiment = sentiment;
        this.summary = summary;
        this.attentionPoint = attentionPoint;
        this.transcript = transcript;
        this.opportunities = opportunities;
        this.risks = risks;
        this.pendingItems = pendingItems;
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

    public String getSummary() {
        return summary;
    }

    public String getAttentionPoint() {
        return attentionPoint;
    }

    public String getTranscript() {
        return transcript;
    }

    public List<OportunidadeDTO> getOpportunities() {
        return opportunities;
    }

    public List<RiscoDTO> getRisks() {
        return risks;
    }

    public List<PendenciaDTO> getPendingItems() {
        return pendingItems;
    }

    public static class OportunidadeDTO {
        private String tag;
        private String description;

        public OportunidadeDTO(String tag, String description) {
            this.tag = tag;
            this.description = description;
        }

        public String getTag() {
            return tag;
        }

        public String getDescription() {
            return description;
        }
    }

    public static class RiscoDTO {
        private String level;
        private String description;

        public RiscoDTO(String level, String description) {
            this.level = level;
            this.description = description;
        }

        public String getLevel() {
            return level;
        }

        public String getDescription() {
            return description;
        }
    }

    public static class PendenciaDTO {
        private int id;
        private String description;
        private String owner;
        private String status;

        public PendenciaDTO(int id, String description, String owner, String status) {
            this.id = id;
            this.description = description;
            this.owner = owner;
            this.status = status;
        }

        public int getId() {
            return id;
        }

        public String getDescription() {
            return description;
        }

        public String getOwner() {
            return owner;
        }

        public String getStatus() {
            return status;
        }
    }
}