package br.com.lucasfaria.sboot_face_comparator.v2;

import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.CompletableFuture;

@Service
public class FaceRecognitionService {

    public CompletableFuture<ComparisonFacesResponse> compareFacesAsync(String image1, String image2) {
        return CompletableFuture.supplyAsync(() -> compareFaces(image1, image2));
    }

    private ComparisonFacesResponse compareFaces(String image1, String image2) {
        // Comando para rodar o script Python
        ProcessBuilder processBuilder = new ProcessBuilder("python3", "src/face_recognition/compare_faces_api.py", image1, image2);
        try {
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            StringBuilder output = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
            process.waitFor();
            // Se a resposta for bem-sucedida, retornamos o status e a mensagem
            return new ComparisonFacesResponse("success", output.toString().trim());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return new ComparisonFacesResponse("error", "Erro ao executar a comparação de faces: " + e.getMessage());
        }
    }


    public CountFacesResponse countFaces(String imagePath) {

        CountFacesResponse response = new CountFacesResponse();

        try {
             ProcessBuilder processBuilder = new ProcessBuilder("python3", "src/face_recognition/detect_faces_api.py", imagePath);
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
