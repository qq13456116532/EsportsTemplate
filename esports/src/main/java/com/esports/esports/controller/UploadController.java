// src/main/java/com/esports/esports/controller/UploadController.java
package com.esports.esports.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/upload")
@RequiredArgsConstructor
public class UploadController {

    @Value("${app.upload-dir:uploads}")     // 默认 ./uploads
    private String uploadDir;

    /** POST /upload/avatar  */
    @PostMapping("/avatar")
    public ResponseEntity<?> uploadAvatar(@RequestParam("file") MultipartFile file,
                                          HttpServletRequest req) throws Exception {

        if (file.isEmpty()) return ResponseEntity.badRequest().body("file is empty");

        // ① 生成文件名并保存
        String ext  = FilenameUtils.getExtension(file.getOriginalFilename());
        String name = UUID.randomUUID() + "." + ext;
        Path full   = Paths.get(uploadDir, name);
        Files.createDirectories(full.getParent());
        file.transferTo(full);

        // ② 拼出可访问 URL（这里假设用静态资源映射 /static/** → uploads/**）
        String base = req.getScheme() + "://" + req.getServerName() + ":" +
                      req.getServerPort() + req.getContextPath();   // http://host:port/api
        String url  = base + "/static/" + name;

        return ResponseEntity.ok(Map.of("url", url));
    }
}
