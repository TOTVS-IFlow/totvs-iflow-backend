package org.example.dto;

public class ReunioesPorMesDTO {

    private String month;
    private int count;

    public ReunioesPorMesDTO(String month, int count) {
        this.month = month;
        this.count = count;
    }

    public String getMonth() {
        return month;
    }

    public int getCount() {
        return count;
    }
}