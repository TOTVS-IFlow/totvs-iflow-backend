package org.example.dto;

public class PendenciaResumoDTO {
    private int id;
    private String description;
    private String owner;
    private String status;
    private int meetingId;
    private String client;

    public PendenciaResumoDTO(
            int id,
            String description,
            String owner,
            String status,
            int meetingId,
            String client) {
        this.id = id;
        this.description = description;
        this.owner = owner;
        this.status = status;
        this.meetingId = meetingId;
        this.client = client;
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

    public int getMeetingId() {
        return meetingId;
    }

    public String getClient() {
        return client;
    }
}