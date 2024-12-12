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

    @Autowired
    private PythonScriptExecutor pythonScriptExecutor;

    @PostMapping("/compare-from-py")
    public int compareFaces(@RequestParam("image1") MultipartFile image1,
                            @RequestParam("image2") MultipartFile image2) {
        return faceComparisonService.compareFaces(image1, image2);
    }

    @GetMapping("/api/recognize-faces")
    public String recognizeFaces() {
        try {
            String result = pythonScriptExecutor.runPythonScript();
            return result;
        } catch (Exception e) {
            return "Erro ao executar o script Python: " + e.getMessage();
        }
    }
}
