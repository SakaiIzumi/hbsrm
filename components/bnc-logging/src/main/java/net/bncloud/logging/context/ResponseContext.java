package net.bncloud.logging.context;

import java.time.Instant;

public class ResponseContext {

    private String response;
    private Instant responseAt;
    private Boolean success;
    private String exception;

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public Instant getResponseAt() {
        return responseAt;
    }

    public void setResponseAt(Instant responseAt) {
        this.responseAt = responseAt;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getException() {
        return exception;
    }

    public void setException(String exception) {
        this.exception = exception;
    }

    @Override
    public String toString() {
        return "ResponseContext{" +
                "response='" + response + '\'' +
                ", responseAt=" + responseAt +
                ", success=" + success +
                ", exception='" + exception + '\'' +
                '}';
    }
}
