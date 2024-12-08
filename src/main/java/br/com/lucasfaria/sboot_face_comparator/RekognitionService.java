package br.com.lucasfaria.sboot_face_comparator;

import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RekognitionService {

    private final AmazonRekognition rekognitionClient;
    private final static Float SIMILARITY = 90F;
    private final AwsConfiguration awsConfiguration;  // Adicionei essa dependência

    @Autowired
    public RekognitionService(AwsConfiguration awsConfiguration) {
        this.awsConfiguration = awsConfiguration;
        this.rekognitionClient = awsConfiguration.amazonRekognition();
    }

    public List<String> compareFaces(String sourceBucket, String sourcePhoto, String targetBucket, List<String> targetPhotos) {

        List<String> resultList = new ArrayList<>();
        Image sourceImage = new Image().withS3Object(new S3Object().withBucket(sourceBucket).withName(sourcePhoto));

        for (String targetPhoto : targetPhotos) {
            Image targetImage = new Image().withS3Object(new S3Object().withBucket(targetBucket).withName(targetPhoto));

            // Faz a solicitação para comparar as faces
            CompareFacesRequest request = new CompareFacesRequest()
                    .withSourceImage(sourceImage)
                    .withTargetImage(targetImage)
                    .withSimilarityThreshold(SIMILARITY); // Similaridade mínima de 90%

            CompareFacesResult result = null;
            try {
                result = rekognitionClient.compareFaces(request);
            } catch (AmazonRekognitionException e) {
                throw new RuntimeException("Erro ao comparar faces com AWS Rekognition", e);
            }

            // Adiciona as similaridades ao resultado
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