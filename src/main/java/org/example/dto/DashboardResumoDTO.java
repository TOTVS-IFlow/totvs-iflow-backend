package org.example.dto;

import java.util.List;

public class DashboardResumoDTO {

    private int meetingCount;
    private int openPendingCount;
    private int opportunityCount;
    private int highRiskCount;
    private List<ReunioesPorMesDTO> meetingsPerMonth;
    private List<SentimentoDTO> sentimentDistribution;

    public DashboardResumoDTO(
            int meetingCount,
            int openPendingCount,
            int opportunityCount,
            int highRiskCount,
            List<ReunioesPorMesDTO> meetingsPerMonth,
            List<SentimentoDTO> sentimentDistribution) {

        this.meetingCount = meetingCount;
        this.openPendingCount = openPendingCount;
        this.opportunityCount = opportunityCount;
        this.highRiskCount = highRiskCount;
        this.meetingsPerMonth = meetingsPerMonth;
        this.sentimentDistribution = sentimentDistribution;
    }

    public int getMeetingCount() {
        return meetingCount;
    }

    public int getOpenPendingCount() {
        return openPendingCount;
    }

    public int getOpportunityCount() {
        return opportunityCount;
    }

    public int getHighRiskCount() {
        return highRiskCount;
    }

    public List<ReunioesPorMesDTO> getMeetingsPerMonth() {
        return meetingsPerMonth;
    }

    public List<SentimentoDTO> getSentimentDistribution() {
        return sentimentDistribution;
    }
}