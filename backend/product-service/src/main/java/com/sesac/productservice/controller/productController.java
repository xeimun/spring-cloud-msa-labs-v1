package com.sesac.productservice.controller;

import com.sesac.productservice.entity.Product;
import com.sesac.productservice.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class productController {

    private final ProductService productService;

    @GetMapping
    @Operation(summary = "상품 목록 조회", description = "모든 상품 정보를 조회합니다.")
    public ResponseEntity<List<Product>> getAllProducts() {
        return ResponseEntity.ok(productService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "상품 조회", description = "ID로 상품 정보를 조회합니다.")
    public ResponseEntity<Product> getProduct(@PathVariable long id) {
        try {
            Product product = productService.findById(id);
            return ResponseEntity.ok(product);

        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
