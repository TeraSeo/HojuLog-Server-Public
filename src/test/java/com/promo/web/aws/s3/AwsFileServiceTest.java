package com.promo.web.aws.s3;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AwsFileServiceTest {

    @Autowired
    private AwsFileService awsFileService;

    @Test
    void saveImageInAWSS3() throws IOException {
        MockMultipartFile multipartFile1 = new MockMultipartFile("file1", "test1.txt", "text/plain", "test file".getBytes(StandardCharsets.UTF_8) );
        String fileUrl = awsFileService.uploadPostFile(multipartFile1, "seotj0413@gmail.com");

        assertEquals(fileUrl.isEmpty(), false);
    }

}