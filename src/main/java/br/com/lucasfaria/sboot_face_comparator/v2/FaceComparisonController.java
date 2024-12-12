package br.com.lucasfaria.sboot_face_comparator.v2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v2")
public class FaceComparisonController {

    @Autowired
    private FaceComparisonService faceComparisonService;

    @PostMapping("/compare-faces")
    public String compareFaces(@RequestParam("image1") String image1,
                               @RequestParam("image2") String image2) {
        return faceComparisonService.compareFaces(image1, image2);
    }

}
