package com.aimage.domain.image.service;

import com.aimage.util.exception.AimageException;
import io.awspring.cloud.s3.ObjectMetadata;
import io.awspring.cloud.s3.S3Resource;
import io.awspring.cloud.s3.S3Template;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;

import static com.aimage.util.exception.ErrorCode.IMAGE_SAVE_FAILED;

@Slf4j
@Component
@RequiredArgsConstructor
public class AmazonS3Service {

    @Value("${spring.cloud.aws.s3.bucket}")
    private String BUCKET_NAME;

    private final S3Template s3Template;

    public String upload(String imageKey, InputStream imageInputStream) {
        ObjectMetadata metadata = ObjectMetadata.builder()
                .expires(Instant.now().plusSeconds(3600))
                .build();

        S3Resource imageResource = s3Template.upload(BUCKET_NAME, imageKey, imageInputStream, metadata);

        try {
            return imageResource.getURL().toString();
        } catch (IOException e) {
            // TODO: need to find better way to handle exception
            throw new AimageException(IMAGE_SAVE_FAILED);
        }
    }

    public void uploadPermanently(String imageKey) {
        S3Resource imageResource = s3Template.download(BUCKET_NAME, imageKey);

        if (imageResource.exists()) {
            try {
                s3Template.upload(BUCKET_NAME, imageKey, imageResource.getInputStream());
            } catch (IOException e) {
                // TODO: need to find better way to handle exception
                throw new AimageException(IMAGE_SAVE_FAILED);
            }
        }
    }
}
