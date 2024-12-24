package br.com.lucasfaria.sboot_face_comparator.v2;

import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;

@Service
public class CountFacesService {

    private static final String PYTHON3 = "src/face_recognition/face-recognition-env/bin/python3";
    private static final String SCRIPT_PYTHON = "src/face_recognition/detect_faces_api.py";

    public CountFacesResponse countFaces(String imagePath) {

        CountFacesResponse response = new CountFacesResponse();

        try {
             ProcessBuilder processBuilder = new ProcessBuilder(PYTHON3, SCRIPT_PYTHON, imagePath);
            processBuilder.redirectErrorStream(true);

            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder output = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }

            if (process.waitFor() == 0) {
                String result = output.toString();
                if (result.contains("status")) {
                    response.setStatus("success");
                    response.setMessage("Faces detectadas com sucesso.");
                    response.setFaceCount(getFaceCountFromJson(result)); // Implementar a lógica de extração
                    response.setFaceLocations(getFaceLocationsFromJson(result)); // Implementar a lógica de extração
                }
            } else {
                response.setStatus("error");
                response.setMessage("Erro ao executar o script Python");
            }
        } catch (Exception e) {
            response.setStatus("error");
            response.setMessage("Erro ao processar a solicitação: " + e.getMessage());
        }

        return response;
    }

    private int getFaceCountFromJson(String json) {
        int faceCount = 0;
        try {
            if (json.contains("face_count")) {
                String[] parts = json.split("face_count\":");
                String countStr = parts[1].split(",")[0].trim();
                faceCount = Integer.parseInt(countStr);
            }
        } catch (Exception e) {
        }
        return faceCount;
    }

    private Object getFaceLocationsFromJson(String json) {
        return json.contains("face_locations") ? json.split("face_locations\":")[1].split("]")[0] : null;
    }

}
