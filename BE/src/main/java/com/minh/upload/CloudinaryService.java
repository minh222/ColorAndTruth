package com.minh.upload;

import com.cloudinary.Cloudinary;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

import static com.minh.config.Exception.http;

@Service
public class CloudinaryService {
    private final Cloudinary cloudinary;

    public CloudinaryService(@Value("${cloudinary.cloud-name}") String cloudName,
                             @Value("${cloudinary.api-key}") String apiKey,
                             @Value("${cloudinary.api-secret}") String apiSecret) {
        Map<String, String> config = new HashMap<>();
        config.put("cloud_name", cloudName);
        config.put("api_key", apiKey);
        config.put("api_secret", apiSecret);
        this.cloudinary = new Cloudinary(config);
    }

    public String upload(MultipartFile file, String publicId) {
        Map<String, Object> options = new HashMap<>();
        options.put("public_id", publicId);
        options.put("overwrite", true);

        Map<String, Object> result = upload(file, options);
        return (String) result.get("secure_url");
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> upload(MultipartFile file, Map<String, Object> options) {
        try {
            byte[] fileBytes = file.getBytes();
            return this.cloudinary.uploader().upload(fileBytes, options);
        } catch (Exception e) {
            throw http(503, "Cloudinary upload failed");
        }
    }
}
