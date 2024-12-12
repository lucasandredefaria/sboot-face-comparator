package br.com.lucasfaria.sboot_face_comparator.v2;

import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;

@Service
public class PythonScriptExecutor {

    public String runPythonScript() throws Exception {
        String pythonScriptPath = "src/main/resources/libs/face_recognition_api.py";

        ProcessBuilder processBuilder = new ProcessBuilder("python3", pythonScriptPath);
        processBuilder.redirectErrorStream(true);

        Process process = processBuilder.start();
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

        StringBuilder output = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            output.append(line).append("\n");
        }

        process.waitFor();
        return output.toString();
    }
}
