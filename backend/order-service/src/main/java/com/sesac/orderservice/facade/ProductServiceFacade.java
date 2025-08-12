package com.sesac.orderservice.facade;

import com.sesac.orderservice.client.ProductServiceClient;
import com.sesac.orderservice.client.dto.ProductDto;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductServiceFacade {

    private final ProductServiceClient productServiceClient;

    @CircuitBreaker(name = "product-service", fallbackMethod = "getProductFallback")
    @Retry(name = "product-service")
    public ProductDto getProductWithFallback(Long productId) {
        return productServiceClient.getProductById(productId);
    }

    public ProductDto getProductFallback(Long productId, Throwable throwable) {

        ProductDto tempProduct = new ProductDto();
        tempProduct.setId(productId);
        tempProduct.setName("Temp Product");
        tempProduct.setPrice(BigDecimal.valueOf(250000));
        tempProduct.setStockQuantity(1);

        return tempProduct;

    }
}
