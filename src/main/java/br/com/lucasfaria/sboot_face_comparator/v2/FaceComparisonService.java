package br.com.lucasfaria.sboot_face_comparator.v2;

import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

@Service
public class FaceComparisonService {

   public String compareFaces(String image1, String image2) {
       try {
           String command = String.format("python3 src/face_recognition/compare_faces.py %s %s", image1, image2);

           Process process = Runtime.getRuntime().exec(command);

           // Ler a saída do script Python
           BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
           String line;
           StringBuilder output = new StringBuilder();
           while ((line = reader.readLine()) != null) {
               output.append(line).append("\n");
           }
           process.waitFor();

           // Retornar o resultado
           return output.toString().trim();
       } catch (Exception e) {
           e.printStackTrace();
           return "Erro ao executar a comparação de faces.";
       }
   }
}
