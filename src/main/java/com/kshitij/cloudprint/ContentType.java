package com.kshitij.cloudprint;

import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class ContentType {
    HashMap<String, String> typeMap;

    public ContentType() {
        typeMap = new HashMap<>();
        typeMap.put("pdf", "application/pdf");
        typeMap.put("jpg", "image/jpeg");
        typeMap.put("jpeg", "image/jpeg");
        typeMap.put("png", "image/png");
        typeMap.put("rtf", "application/rtf");
        typeMap.put("doc", "application/msword");
    }

    public String getContentType(String ext) {
        return typeMap.get(ext);
    }
}
