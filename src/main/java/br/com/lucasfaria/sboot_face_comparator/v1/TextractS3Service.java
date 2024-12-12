package br.com.lucasfaria.sboot_face_comparator.v1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.textract.TextractClient;
import software.amazon.awssdk.services.textract.model.BlockType;
import software.amazon.awssdk.services.textract.model.DetectDocumentTextRequest;
import software.amazon.awssdk.services.textract.model.DetectDocumentTextResponse;
import software.amazon.awssdk.services.textract.model.Document;

import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TextractS3Service {

    private final TextractClient textractClient;
    private final S3Client s3Client;

    @Autowired
    public TextractS3Service(TextractClient textractClient, S3Client s3Client) {
        this.textractClient = textractClient;
        this.s3Client = s3Client;
    }

    public String detectTextFromS3Image(String bucketName, String objectKey) {
        try {
            InputStream imageStream = getS3Object(bucketName, objectKey);
            return detectTextFromImage(imageStream);
        } catch (Exception e) {
            return "Erro ao processar a imagem: " + e.getMessage();
        }
    }

    private InputStream getS3Object(String bucketName, String objectKey) {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(objectKey)
                .build();
        return s3Client.getObject(getObjectRequest);
    }

    private String detectTextFromImage(InputStream imageStream) {
        Document document = Document.builder().bytes(SdkBytes.fromInputStream(imageStream)).build();
        DetectDocumentTextRequest request = DetectDocumentTextRequest.builder().document(document).build();
        DetectDocumentTextResponse response = textractClient.detectDocumentText(request);

        List<String> lines = response.blocks().stream()
                .filter(block -> block.blockType() == BlockType.LINE)
                .map(block -> block.text())
                .collect(Collectors.toList());

        return String.join("\n", lines);
    }
}