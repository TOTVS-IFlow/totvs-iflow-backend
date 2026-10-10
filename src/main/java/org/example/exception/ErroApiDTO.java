package org.example.exception;

public class ErroApiDTO {
    private int status;
    private String error;
    private String message;

    public ErroApiDTO(int status, String error, String message) {
        this.status = status;
        this.error = error;
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }
}