package com.cb.controller;

import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

public class FileUploadUtils {
    public static void saveFile(String uploadDir, String fileName, MultipartFile file) throws IOException {
        File directory = new File(uploadDir);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        String filePath = directory.getAbsolutePath() + File.separator + fileName;
        FileCopyUtils.copy(file.getBytes(), new File(filePath));
    }
}
