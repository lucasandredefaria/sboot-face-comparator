package br.com.lucasfaria.sboot_face_comparator;

import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.textract.model.BlockType;
import software.amazon.awssdk.services.textract.model.DetectDocumentTextRequest;
import software.amazon.awssdk.services.textract.model.DetectDocumentTextResponse;
import software.amazon.awssdk.services.textract.model.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RekognitionService {

    private final AmazonRekognition rekognitionClient;
    private final static Float SIMILARITY = 90F;

    @Autowired
    public RekognitionService(AmazonRekognition rekognitionClient) {
        this.rekognitionClient = rekognitionClient;
    }

    public List<String> compareFaces(String sourceBucket, String sourcePhoto, String targetBucket, List<String> targetPhotos) {
        List<String> resultList = new ArrayList<>();
        Image sourceImage = new Image().withS3Object(new S3Object().withBucket(sourceBucket).withName(sourcePhoto));

        for (String targetPhoto : targetPhotos) {
            Image targetImage = new Image().withS3Object(new S3Object().withBucket(targetBucket).withName(targetPhoto));
            CompareFacesRequest request = new CompareFacesRequest()
                    .withSourceImage(sourceImage)
                    .withTargetImage(targetImage)
                    .withSimilarityThreshold(SIMILARITY);

            CompareFacesResult result = null;
            try {
                result = rekognitionClient.compareFaces(request);
            } catch (AmazonRekognitionException e) {
                throw new RuntimeException("Erro ao comparar faces com AWS Rekognition", e);
            }

            List<CompareFacesMatch> faceMatches = result.getFaceMatches();
            if (!faceMatches.isEmpty()) {
                faceMatches.forEach(faceMatch ->
                        resultList.add("Comparing with " + targetPhoto + ": Similarity " + faceMatch.getSimilarity() + "%"));
            } else {
                resultList.add("No match for " + targetPhoto);
            }
        }

        return resultList;
    }
}