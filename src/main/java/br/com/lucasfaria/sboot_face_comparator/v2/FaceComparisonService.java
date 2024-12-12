package br.com.lucasfaria.sboot_face_comparator.v2;

import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.CompletableFuture;

@Service
public class FaceComparisonService {

    public CompletableFuture<ComparisonResponse> compareFacesAsync(String image1, String image2) {
        return CompletableFuture.supplyAsync(() -> compareFaces(image1, image2));
    }

    private ComparisonResponse compareFaces(String image1, String image2) {
        // Comando para rodar o script Python
        ProcessBuilder processBuilder = new ProcessBuilder("python3", "src/face_recognition/compare_faces.py", image1, image2);
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
            return new ComparisonResponse("success", output.toString().trim());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return new ComparisonResponse("error", "Erro ao executar a comparação de faces: " + e.getMessage());
        }
    }
}
