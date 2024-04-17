package com.example.foodx_be.service;

import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

@Service
public class CloudiaryServiceImpl implements CloudiaryService {
    Cloudinary cloudinary;

    public CloudiaryServiceImpl() {
        Map<String, String> valuesMap = new HashMap<>();
        valuesMap.put("cloud_name", "dfsdwobb1");
        valuesMap.put("api_key", "147347376796877");
        valuesMap.put("api_secret", "05wBVNxc25InQzDuPCP5R4fZ7n8");
        cloudinary = new Cloudinary(valuesMap);
    }

    @Override
    public List<Map> uploadMultiFiles(MultipartFile[] multipartFiles) throws  IOException {
        List<Map> results = new ArrayList<>();
        for (MultipartFile multipartFile : multipartFiles) {
            File file = convert(multipartFile);
            Map result = cloudinary.uploader().upload(file, ObjectUtils.emptyMap());
            results.add(result);
            if (!Files.deleteIfExists(file.toPath())) {
                throw new IOException("Failed to delete temporary file: " + file.getAbsolutePath());
            }
        }
        return results;
    }

    @Override
    public Map delete(String id) throws  IOException {
        return cloudinary.uploader().destroy(id, ObjectUtils.emptyMap());
    }

    @Override
    public File convert(MultipartFile multipartFile) throws  IOException {
        File file = new File(Objects.requireNonNull(multipartFile.getOriginalFilename()));
        FileOutputStream fo = new FileOutputStream(file);
        fo.write(multipartFile.getBytes());
        fo.close();
        return file;
    }
}
