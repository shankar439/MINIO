package com.shankar.minio.file;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

@RestController
public class MinioController {

    @Autowired
    FileUpload fileUpload;

    @PostMapping("/upload")
    public String uploadFile(@RequestPart MultipartFile file) throws Exception {

        if (file.getOriginalFilename() != null) {
            fileUpload.putObject(file.getOriginalFilename(), file.getBytes(), file);
            return "success";
        }
        throw new Exception("File cannot be Upload");
    }


    @GetMapping("/download")
    public void download(HttpServletResponse response) {

            fileUpload.getObject(response);

    }

}
