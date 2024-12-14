package br.com.lucasfaria.sboot_face_comparator.v2;

import org.springframework.stereotype.Service;

import org.springframework.stereotype.Service;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

@Service
public class FaceRecognitionDirectoryService {

    public String processDirectory(String directoryPath) {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("python3", "src/face_recognition/face_recognition_text_image_processor.py", directoryPath);
            processBuilder.redirectErrorStream(true);

            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            StringBuilder output = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }

            int exitCode = process.waitFor();
            if (exitCode == 0) {
                return output.toString(); // Retorna a saída do script
            } else {
                return "Erro ao executar o script Python";
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return "Erro ao processar o diretório: " + e.getMessage();
        }
    }
}
