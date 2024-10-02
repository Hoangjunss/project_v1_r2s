package com.r2s.project_v1.controller;

import com.r2s.project_v1.dto.request.product.CreateProductRequest;
import com.r2s.project_v1.models.ProductImage;
import com.r2s.project_v1.services.product.productService.ProductImageService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v1/image")
@RestController
public class ProjectImageController {
    @Autowired
    private ProductImageService productImageService;
    @GetMapping()
    public ResponseEntity<?> get(
            @RequestParam Integer id) {

        ProductImage image = productImageService.get(id);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(image.getImageType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + image.getImageName() + "\"")
                .body(new ByteArrayResource(image.getImageData()));
    }
}
