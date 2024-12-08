package br.com.lucasfaria.sboot_face_comparator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class RekognitionController {

    private final RekognitionService rekognitionService;

    @Autowired
    public RekognitionController(RekognitionService rekognitionService) {
        this.rekognitionService = rekognitionService;
    }

    @GetMapping("/compare-faces")
    public List<String> compareFacesWithMultipleImages(
            @RequestParam String sourceBucket,
            @RequestParam String sourcePhoto,
            @RequestParam String targetBucket,
            @RequestParam List<String> targetPhotos) {

        return rekognitionService.compareFaces(sourceBucket, sourcePhoto, targetBucket, targetPhotos);
    }
}