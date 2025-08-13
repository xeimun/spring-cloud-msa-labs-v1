package com.sesac.orderservice.service;

import com.sesac.orderservice.client.dto.ProductDto;
import com.sesac.orderservice.client.dto.UserDto;
import com.sesac.orderservice.dto.OrderRequestDto;
import com.sesac.orderservice.entity.Order;
import com.sesac.orderservice.event.OrderCreatedEvent;
import com.sesac.orderservice.event.OrderEventPublisher;
import com.sesac.orderservice.facade.ProductServiceFacade;
import com.sesac.orderservice.facade.UserServiceFacade;
import com.sesac.orderservice.repository.OrderRepository;
import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;
import java.math.BigDecimal;
import java.time.LocalDateTime;
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
    private final Tracer tracer;
    private final OrderEventPublisher orderEventPublisher;

    @Transactional(readOnly = true)
    public Order findById(Long id) {
        return orderRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Order not found with id" + id)
        );
    }

    // 주문 생성 (고객이 주문했을 때)
    public Order createOrder(OrderRequestDto request) {

        Span span = tracer.nextSpan()
                          .name("createOrder")
                          .tag("order userId", request.getUserId())
                          .tag("productId", request.getProductId())
                          .start();

        try (Tracer.SpanInScope ws = tracer.withSpan(span)) {
            UserDto user = userServiceFacade.getUserWithFallback(request.getUserId());
            if (user == null) {
                throw new RuntimeException("User not found");
            }

            ProductDto product = productServiceFacade.getProductWithFallback(request.getProductId());
            if (product == null) {
                throw new RuntimeException("Product not found");
            }

            // if (product.getStockQuantity() < request.getQuantity()) {
            //     throw new RuntimeException("Product has less than stock quantity");
            // }

            Order order = new Order();
            order.setUserId(user.getId());
            order.setTotalAmount(product.getPrice().multiply(BigDecimal.valueOf(request.getQuantity())));
            order.setStatus("COMPLETED");

            OrderCreatedEvent event = new OrderCreatedEvent(
                    order.getId(),
                    request.getUserId(),
                    request.getProductId(),
                    request.getQuantity(),
                    order.getTotalAmount(),
                    LocalDateTime.now()
            );
            orderEventPublisher.publishOrderCreated(event);

            return orderRepository.save(order);

        } catch (Exception e) {
            span.tag("error", e.getMessage());
            throw e;
        } finally {
            span.end();
        }
    }

    public List<Order> getOrders(Long userId) {
        return orderRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }
}
