package br.com.lucasfaria.sboot_face_comparator.v2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/v2")
public class FaceComparisonController {

    @Autowired
    private FaceComparisonService faceComparisonService;

    @PostMapping("/compare-faces")
    public CompletableFuture<ResponseEntity<ComparisonResponse>> compareFaces(@RequestParam("image1") String image1,
                                                                              @RequestParam("image2") String image2) {
        // Chama o serviço de comparação de faces de forma assíncrona e retorna o ResponseEntity com o ComparisonResponse
        return faceComparisonService.compareFacesAsync(image1, image2)
                .thenApply(ResponseEntity::ok);
    }
}
