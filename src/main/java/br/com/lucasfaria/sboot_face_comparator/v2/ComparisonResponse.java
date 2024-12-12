package br.com.lucasfaria.sboot_face_comparator.v2;

public class ComparisonResponse {
    private String status;
    private String message;

    public ComparisonResponse(String status, String message) {
        this.status = status;
        this.message = message;
    }

    public ComparisonResponse(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
