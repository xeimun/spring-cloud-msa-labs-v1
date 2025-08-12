package com.sesac.orderservice.service;

import com.sesac.orderservice.client.dto.ProductDto;
import com.sesac.orderservice.client.dto.UserDto;
import com.sesac.orderservice.dto.OrderRequestDto;
import com.sesac.orderservice.entity.Order;
import com.sesac.orderservice.facade.ProductServiceFacade;
import com.sesac.orderservice.facade.UserServiceFacade;
import com.sesac.orderservice.repository.OrderRepository;
import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {
    private final OrderRepository orderRepository;
    private final UserServiceFacade userServiceFacade;
    private final ProductServiceFacade productServiceFacade;

    @Transactional(readOnly = true)
    public Order findById(Long id) {
        return orderRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Order not found with id" + id)
        );
    }

    // 주문 생성 (고객이 주문했을 때)
    public Order createOrder(OrderRequestDto request) {
        UserDto user = userServiceFacade.getUserWithFallback(request.getUserId());
        if (user == null) {
            throw new RuntimeException("User not found");
        }

        ProductDto product = productServiceFacade.getProductWithFallback(request.getProductId());
        if (product == null) {
            throw new RuntimeException("Product not found");
        }

        if (product.getStockQuantity() < request.getQuantity()) {
            throw new RuntimeException("Product has less than stock quantity");
        }

        Order order = new Order();
        order.setUserId(user.getId());
        order.setTotalAmount(product.getPrice().multiply(BigDecimal.valueOf(request.getQuantity())));
        order.setStatus("COMPLETED");

        return orderRepository.save(order);
    }

    public List<Order> getOrders(Long userId) {
        return orderRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }
}
