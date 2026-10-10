package org.example.dto;

public class ClienteResumoDTO {
    private int id;
    private String name;
    private String sector;
    private String product;
    private String sentiment;
    private int meetingCount;
    private int openPendingCount;
    private int highRiskCount;

    public ClienteResumoDTO(
            int id, String name, String sector, String product,
            String sentiment, int meetingCount,
            int openPendingCount, int highRiskCount) {
        this.id = id;
        this.name = name;
        this.sector = sector;
        this.product = product;
        this.sentiment = sentiment;
        this.meetingCount = meetingCount;
        this.openPendingCount = openPendingCount;
        this.highRiskCount = highRiskCount;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getSector() { return sector; }
    public String getProduct() { return product; }
    public String getSentiment() { return sentiment; }
    public int getMeetingCount() { return meetingCount; }
    public int getOpenPendingCount() { return openPendingCount; }
    public int getHighRiskCount() { return highRiskCount; }
}