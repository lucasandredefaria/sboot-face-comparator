package br.com.lucasfaria.sboot_face_comparator.v2;

import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.*;

@Service
public class FaceComparisonService {

    private static final String FLASK_API_URL = "http://127.0.0.1:5000/compare-faces";

    public int compareFaces(MultipartFile image1, MultipartFile image2) {
        RestTemplate restTemplate = new RestTemplate();

        // Criar um MultiValueMap para enviar as imagens
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("image1", image1.getResource());
        body.add("image2", image2.getResource());

        // Criar a requisição com as imagens
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        // Enviar a requisição POST para a API Flask
        ResponseEntity<String> response = restTemplate.exchange(FLASK_API_URL, HttpMethod.POST, requestEntity, String.class);

        // Parse o resultado da resposta JSON
        String responseBody = response.getBody();
        if (responseBody != null && responseBody.contains("similarity")) {
            // Retornar a similaridade (1 ou 0)
            return Integer.parseInt(responseBody.split(":")[1].trim().replace("}", "").trim());
        }

        return -1;  // Retorna -1 em caso de erro
    }
}
