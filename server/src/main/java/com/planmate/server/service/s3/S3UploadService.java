package com.planmate.server.service.s3;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface S3UploadService {
    public String upload(MultipartFile multipartFile, String dirName) throws IOException;
}
