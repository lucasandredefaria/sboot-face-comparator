package br.com.lucasfaria.sboot_face_comparator.v2;

import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.CompletableFuture;

@Service
public class CompareFacesService {

    private static final String PYTHON3 = "src/face_recognition/face-recognition-env/bin/python3";
    private static final String SCRIPT_PYTHON = "src/face_recognition/compare_faces_api.py";

    public CompletableFuture<CompareFacesResponse> compareFacesAsync(String image1, String image2) {
        return CompletableFuture.supplyAsync(() -> compareFaces(image1, image2));
    }

    private CompareFacesResponse compareFaces(String image1, String image2) {
        ProcessBuilder processBuilder = new ProcessBuilder(PYTHON3, SCRIPT_PYTHON, image1, image2);
        try {
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            StringBuilder output = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
            process.waitFor();
            return new CompareFacesResponse("success", output.toString().trim());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return new CompareFacesResponse("error", "Erro ao executar a comparação de faces: " + e.getMessage());
        }
    }

}
