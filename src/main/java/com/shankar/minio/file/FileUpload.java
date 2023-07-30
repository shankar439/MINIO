package com.shankar.minio.file;

import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.messages.Bucket;
import lombok.SneakyThrows;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.List;
import java.util.UUID;

@Service
public class FileUpload {


    @Autowired
    MinioClient minioClient;

    @Value("${minio.buckek.name}")
    String defaultBucketName;

    @Value("${minio.default.folder}")
    String defaultBaseFolder;

    public List<Bucket> getAllBuckets() {
        try {
            return minioClient.listBuckets();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

    }


    public void putObject(String name, byte[] content, MultipartFile multipartFile) {

        String fileType = multipartFile.getContentType();
        try {
            String objectName = UUID.randomUUID().toString().replaceAll("-", "")
                    + name.substring(name.lastIndexOf("."));

            InputStream inputStream = new ByteArrayInputStream(content);
            minioClient.putObject(PutObjectArgs.builder().bucket(defaultBucketName).object(objectName)
                           .stream(inputStream, -1, 1073741824)
                            .contentType(fileType)
                            .build());
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

    }

    @SneakyThrows
    public void getObject(HttpServletResponse response) {

                InputStream stream =
                        minioClient.getObject(GetObjectArgs.builder()
                                .bucket(defaultBucketName)
                                .object("304e5e22a8ca42c795c445d68624f1dc.png")
                                .build());


        response.setHeader("Content-Disposition", "attachment;filename="
                + URLEncoder.encode("304e5e22a8ca42c795c445d68624f1dc.png", "UTF-8"));
        response.setCharacterEncoding("UTF-8");
        IOUtils.copy(stream, response.getOutputStream());
        stream.close();

    }


}
