package com.SEERGEEVA.bankBot;

public class EngineResponse {

    public String message;
    public boolean isSuccess;

    public EngineResponse(String message, boolean isSuccess) {
        this.message = message;
        this.isSuccess = isSuccess;
    }
}
