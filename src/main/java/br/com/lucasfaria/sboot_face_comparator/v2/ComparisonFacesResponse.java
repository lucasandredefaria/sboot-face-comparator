package br.com.lucasfaria.sboot_face_comparator.v2;

public class ComparisonFacesResponse {
    private String status;
    private String message;

    public ComparisonFacesResponse(String status, String message) {
        this.status = status;
        this.message = message;
    }

    public ComparisonFacesResponse(String message) {
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
