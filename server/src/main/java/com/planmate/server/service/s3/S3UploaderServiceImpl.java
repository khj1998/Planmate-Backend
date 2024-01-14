package com.planmate.server.service.s3;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class S3UploaderServiceImpl implements S3UploadService {
    private final AmazonS3 amazonS3Client;
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Override
    public String upload(MultipartFile multipartFile, String dirName) throws IOException {
        String fileName = multipartFile.getOriginalFilename();

        String ext = fileName.split("\\.")[1];
        String contentType = "";

        switch (ext) {
            case "jpeg":
                contentType = "image/jpeg";
                break;
            case "png":
                contentType = "image/png";
                break;
            case "txt":
                contentType = "text/plain";
                break;
            case "csv":
                contentType = "text/csv";
                break;
        }

        try {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(contentType);

            amazonS3Client.putObject(new PutObjectRequest(bucket, dirName + "/" + fileName, multipartFile.getInputStream(), metadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
        } catch (AmazonServiceException e) {
            e.printStackTrace();
        } catch (SdkClientException e) {
            e.printStackTrace();
        }

        //object 정보 가져오기
        ListObjectsV2Result listObjectsV2Result = amazonS3Client.listObjectsV2(bucket);
        List<S3ObjectSummary> objectSummaries = listObjectsV2Result.getObjectSummaries();

        for (S3ObjectSummary object: objectSummaries) {
            System.out.println("object = " + object.toString());
        }
        return amazonS3Client.getUrl(bucket, dirName + "/" + fileName).toString();
    }

}
