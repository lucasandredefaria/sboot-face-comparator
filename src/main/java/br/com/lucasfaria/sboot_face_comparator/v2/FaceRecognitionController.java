package br.com.lucasfaria.sboot_face_comparator.v2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/v2")
public class FaceRecognitionController {

    @Autowired
    private FaceRecognitionService faceComparisonService;

    @PostMapping("/compare-faces")
    public CompletableFuture<ResponseEntity<ComparisonFacesResponse>> compareFaces(@RequestParam("image1") String image1,
                                                                                   @RequestParam("image2") String image2) {
        return faceComparisonService.compareFacesAsync(image1, image2)
                .thenApply(ResponseEntity::ok);
    }

    @PostMapping("/count-faces")
    public ResponseEntity<CountFacesResponse> compareFaces(@RequestParam String imagePath) {
        CountFacesResponse response = faceComparisonService.countFaces(imagePath);
        if ("success".equals(response.getStatus()))
            return ResponseEntity.ok(response);
        else
            return ResponseEntity.status(500).body(response);
    }

}
