package com.sesac.productservice.service;

import com.sesac.productservice.entity.Product;
import com.sesac.productservice.repository.ProductRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ProductService {
    private final ProductRepository productRepository;

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Product findById(Long id) {
        return productRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Product not found with id" + id)
        );
    }

    public void decreaseStock(Long productId, Integer quantity) {
        Product product = productRepository.findById(productId)
                                           .orElseThrow(
                                                   () -> new RuntimeException("Product not found with id" + productId));

        if (product.getStockQuantity() < quantity) {
            throw new RuntimeException("Not enough stock");
        }

        product.setStockQuantity(product.getStockQuantity() - quantity);

        log.info("재고 차감 완료 - 상품: {}, 남은 재고: {}", product.getName(), product.getStockQuantity());
    }

    public void restoreStock(Long productId, Integer quantity) {
        Product product = productRepository.findById(productId).orElseThrow(
                () -> new RuntimeException("Product not found with id" + productId));

        product.setStockQuantity(product.getStockQuantity() + quantity);
    }
}
