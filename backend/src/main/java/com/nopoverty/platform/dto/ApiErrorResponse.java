package com.nopoverty.platform.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ApiErrorResponse {
    private boolean success = false;
    private String message;
    private LocalDateTime timestamp = LocalDateTime.now();
    private String path;
    private List<String> errors = new ArrayList<>();

    public ApiErrorResponse() {}

    public ApiErrorResponse(String message, String path, List<String> errors) {
        this.success = false;
        this.message = message;
        this.timestamp = LocalDateTime.now();
        this.path = path;
        this.errors = (errors != null) ? errors : new ArrayList<>();
    }

    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    public String getPath() { return path; }
    public void setPath(String path) { this.path = path; }

    public List<String> getErrors() { return errors; }
    public void setErrors(List<String> errors) { this.errors = errors; }
}

