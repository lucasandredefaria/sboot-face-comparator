package br.com.lucasfaria.sboot_face_comparator.v1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class RekognitionController {

    private final RekognitionService rekognitionService;
    private final TextractS3Service textractS3Service;

    @Autowired
    public RekognitionController(RekognitionService rekognitionService, TextractS3Service textractS3Service) {
        this.rekognitionService = rekognitionService;
        this.textractS3Service = textractS3Service;
    }

    @GetMapping("/compare-faces")
    public List<String> compareFacesWithMultipleImages(@RequestParam String sourceBucket,
                                                       @RequestParam String sourcePhoto,
                                                       @RequestParam String targetBucket,
                                                       @RequestParam List<String> targetPhotos) {
        return rekognitionService.compareFaces(sourceBucket, sourcePhoto, targetBucket, targetPhotos);
    }

    @GetMapping("/detect-text")
    public String detectTextFromS3(@RequestParam String sourceBucket,
                                   @RequestParam String objectKey) {
        return textractS3Service.detectTextFromS3Image(sourceBucket, objectKey);
    }
}
