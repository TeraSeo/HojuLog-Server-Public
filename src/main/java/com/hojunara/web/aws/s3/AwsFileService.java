package com.hojunara.web.aws.s3;

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

/**
 * Service for handling file operations with AWS S3, such as uploading and deleting files.
 * <p>
 * This service supports uploading post images and profile images,
 * and deleting profile images from the specified S3 bucket.
 * </p>
 *
 * @author Taejun Seo
 */
@Slf4j
@Service
public class AwsFileService {

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    /**
     * Constructs an AwsFileService with the given Amazon S3 client.
     *
     * @param amazonS3Client the Amazon S3 client
     */
    @Autowired
    public AwsFileService(AmazonS3Client amazonS3Client) {
        this.amazonS3Client = amazonS3Client;
    }

    /**
     * Uploads a post image file to S3 under the user's email-based directory.
     *
     * @param uploadFile the file to be uploaded
     * @param email the email of the user uploading the file
     * @return the URL of the uploaded file, or an empty string if upload fails
     */
    public String uploadPostFile(MultipartFile uploadFile, String email) {
        try {
            String originalFileName = uploadFile.getOriginalFilename();
            String timestamp = String.valueOf(Instant.now().toEpochMilli());
            String uuid = UUID.randomUUID().toString();
            String fileName = email + "/post/" + timestamp + "_" + uuid + "_" + originalFileName;
            String uploadImageUrl = putS3(uploadFile, fileName);
            return uploadImageUrl;
        } catch (RuntimeException e) {
            log.error("Failed to upload post file", e);
            return "";
        }
    }

    /**
     * Uploads a profile image file to S3 under the user's email-based directory.
     *
     * @param uploadFile the file to be uploaded
     * @param email the email of the user uploading the file
     * @return the URL of the uploaded file, or an empty string if upload fails
     */
    public String uploadProfileFile(MultipartFile uploadFile, String email) {
        try {
            String originalFileName = uploadFile.getOriginalFilename();
            String timestamp = String.valueOf(Instant.now().toEpochMilli());
            String uuid = UUID.randomUUID().toString();
            String fileName = email + "/profile/" + timestamp + "_" + uuid + "_" + originalFileName;
            String uploadImageUrl = putS3(uploadFile, fileName);
            return uploadImageUrl;
        } catch (RuntimeException e) {
            log.error("Failed to upload profile file", e);
            return "";
        }
    }

    /**
     * Uploads a file to the configured S3 bucket with the give fileName.
     *
     * @param uploadFile the file to be uploaded
     * @param fileName the S3 object key (path inside the bucket)
     * @return the full URL of the uploaded file in S3
     * @throws RuntimeException if an IOException occurs during upload
     */
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
        }
    }

    /**
     * Removes a profile image file from the S3 bucket based on the user's email and the given file URL.
     *
     * @param email the email of the user whose profile image should be deleted
     * @param fileName the full URL of the file to be deleted
     */
    public void removeProfileFile(String email, String fileName) {
        try {
            if (fileName != null && fileName != "") {
                String fileUrl = email + "/profile/" + fileName.substring(fileName.lastIndexOf('/') + 1);
                amazonS3Client.deleteObject(bucket, fileUrl);
                log.info("Successfully removed profile file with file url: " + fileUrl);
            }
        } catch (Exception e) {
            log.error("Failed to remove profile file");
            throw e;
        }
    }
}