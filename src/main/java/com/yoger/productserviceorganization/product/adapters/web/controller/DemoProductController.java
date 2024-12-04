package com.yoger.productserviceorganization.product.adapters.web.controller;

import com.yoger.productserviceorganization.product.adapters.web.dto.request.DemoProductRequestDTO;
import com.yoger.productserviceorganization.product.adapters.web.dto.request.UpdatedDemoProductRequestDTO;
import com.yoger.productserviceorganization.product.adapters.web.dto.response.DemoProductResponseDTO;
import com.yoger.productserviceorganization.product.adapters.web.dto.response.SimpleDemoProductResponseDTO;
import com.yoger.productserviceorganization.product.application.ProductService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products/demo")
@RequiredArgsConstructor
public class DemoProductController {
    private final ProductService productService;

    @PostMapping
    public ResponseEntity<DemoProductResponseDTO> saveDemo(
            @RequestHeader(value = "User-Id") Long creatorId,
            @Valid @ModelAttribute DemoProductRequestDTO demoProductRequestDTO
    ) {
        DemoProductResponseDTO savedDemoProduct = productService.saveDemoProduct(creatorId, demoProductRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedDemoProduct);
    }

    @GetMapping
    public ResponseEntity<List<SimpleDemoProductResponseDTO>> getDemoProducts() {
        List<SimpleDemoProductResponseDTO> demoProducts = productService.findSimpleDemoProducts();
        return ResponseEntity.ok(demoProducts);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<DemoProductResponseDTO> getDemoProduct(@PathVariable Long productId) {
        DemoProductResponseDTO demoProduct = productService.findDemoProduct(productId);
        return ResponseEntity.ok(demoProduct);
    }

    @PatchMapping("/{productId}")
    public ResponseEntity<DemoProductResponseDTO> updateDemoProduct(
            @PathVariable Long productId,
            @RequestHeader(value = "User-Id") Long creatorId,
            @Valid @ModelAttribute UpdatedDemoProductRequestDTO updatedDemoProductRequestDTO
    ) {
        DemoProductResponseDTO updatedProduct =
                productService.updateDemoProduct(productId, creatorId, updatedDemoProductRequestDTO);
        return ResponseEntity.ok(updatedProduct);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<String> deleteDemoProduct(
            @PathVariable Long productId,
            @RequestHeader(value = "User-Id") Long creatorId
    ) {
        productService.deleteDemoProduct(productId, creatorId);
        return ResponseEntity.noContent().build();
    }
}
