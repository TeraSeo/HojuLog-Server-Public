package com.promo.web.aws.s3;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.time.Instant;
import java.util.UUID;

@Slf4j
@Service
public class AwsFileService {

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Autowired
    public AwsFileService(AmazonS3Client amazonS3Client) {
        this.amazonS3Client = amazonS3Client;
    }

    public String uploadPostFile(MultipartFile uploadFile, String email) {
        String originalFileName = uploadFile.getOriginalFilename();
        String timestamp = String.valueOf(Instant.now().toEpochMilli());
        String uuid = UUID.randomUUID().toString();
        String fileName = email + "/post/" + timestamp + "_" + uuid + "_" + originalFileName;
        String uploadImageUrl = putS3(uploadFile, fileName);
        return uploadImageUrl;
    }

    public String putS3(MultipartFile uploadFile, String fileName) {
        try {
            ObjectMetadata metadata= new ObjectMetadata();
            metadata.setContentType(uploadFile.getContentType());
            metadata.setContentLength(uploadFile.getSize());
            amazonS3Client.putObject(bucket, fileName, uploadFile.getInputStream(), metadata);
            String fileUrl = amazonS3Client.getUrl(bucket, fileName).toString();
            log.info("Successfully uploaded s3 file with file url: " + fileUrl);
            return fileUrl;
        } catch (IOException e) {
            log.error("Failed to upload s3 file with filename: " + fileName);
            throw new RuntimeException(e);
        } catch (Exception e) {
            log.error("Failed to upload s3 file with filename: " + fileName);
            throw e;
        }
    }
}