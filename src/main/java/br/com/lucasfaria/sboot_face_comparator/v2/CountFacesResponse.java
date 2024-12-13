package br.com.lucasfaria.sboot_face_comparator.v2;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CountFacesResponse {

    private String status;
    private int faceCount;
    private String message;
    private Object faceLocations;

}
