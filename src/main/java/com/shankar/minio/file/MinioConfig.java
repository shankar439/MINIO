package com.shankar.minio.file;

import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MinioConfig {
    @Value("${minio.access.name}")
    String accessKey;
    @Value("${minio.access.secret}")
    String accessSecret;
//    @Value("${minio.url}")
    @Value("${minio.endpoint}")
    String minioUrl;

    @Bean
    public MinioClient generateMinioClient() {
        try {
            MinioClient minioClient =
                    MinioClient.builder().endpoint(minioUrl)
                            .credentials(accessKey, accessSecret)
                            .build();
            return minioClient;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

    }
}
