package com.example.foodx_be.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface CloudiaryService {
    List<Map> uploadMultiFiles(MultipartFile[] multipartFiles, String folderName) throws IOException;
    Map uploadFile(MultipartFile multipartFile) throws  IOException;
    Map delete(String id) throws IOException;
    File convert(MultipartFile multipartFile) throws IOException;
}
