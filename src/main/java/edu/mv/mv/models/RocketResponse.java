package edu.mv.mv.models;

public class RocketResponse {

    public int getHttpStatusCode() {
        return httpStatusCode;
    }

    public void setHttpStatusCode(int httpStatusCode) {
        this.httpStatusCode = httpStatusCode;
    }

    private int httpStatusCode;
    private String message;

}
