package br.com.lucasfaria.sboot_face_comparator.v2;

import lombok.Getter;
import lombok.Setter;

public class CountFacesResponse {

    private String status;
    private int faceCount;
    private String message;
    private Object faceLocations;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getFaceCount() {
        return faceCount;
    }

    public void setFaceCount(int faceCount) {
        this.faceCount = faceCount;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getFaceLocations() {
        return faceLocations;
    }

    public void setFaceLocations(Object faceLocations) {
        this.faceLocations = faceLocations;
    }

}
