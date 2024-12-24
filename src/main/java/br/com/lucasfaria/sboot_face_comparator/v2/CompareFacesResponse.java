package br.com.lucasfaria.sboot_face_comparator.v2;

public class CompareFacesResponse {
    private String status;
    private String message;

    public CompareFacesResponse(String status, String message) {
        this.status = status;
        this.message = message;
    }

    public CompareFacesResponse(String message) {
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
