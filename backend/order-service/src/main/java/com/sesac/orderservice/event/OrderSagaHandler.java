package com.sesac.orderservice.event;

import com.sesac.orderservice.entity.Order;
import com.sesac.orderservice.entity.OrderStatus;
import com.sesac.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class OrderSagaHandler {

    private final OrderRepository orderRepository;

    @RabbitListener(queues = "${order.event.queue.inventory-failed}")
    public void handleInventoryFailed(InventoryFailedEvent event) {
        log.info("재고 부족 이벤트 수신 - orderId: {}, reason: {}", event.getOrderId(), event.getReason());

        try {
            Order order = orderRepository.findById(event.getOrderId())
                                         .orElseThrow(() -> new RuntimeException("주문을 찾을 수 없음: " + event.getOrderId()));
            order.setStatus(OrderStatus.CANCELLED);
        } catch (Exception e) {
            log.error("재고 실패 처리 중 오류 - orderId: {}, error: {}", event.getOrderId(), e.getMessage());
        }

    }

    @RabbitListener(queues = "${order.event.queue.payment-completed}")
    public void handlePaymentCompleted(PaymentCompletedEvent event) {
        log.info("결제 완료 이벤트 수신- orderId: {}, amount: {}", event.getOrderId(), event.getAmount());

        try {
            Order order = orderRepository.findById(event.getOrderId())
                                         .orElseThrow(() -> new RuntimeException("주문을 찾을 수 없음: " + event.getOrderId()));
            order.setStatus(OrderStatus.COMPLETED);
        } catch (Exception e) {
            log.error("결제 완료 처리 중 오류 - orderId: {}, error: {}", event.getOrderId(), e.getMessage());
        }
    }

    @RabbitListener(queues = "${order.event.queue.payment-failed}")
    public void handlePaymentFailed(PaymentFailedEvent event) {
        log.info("결제 실패 이벤트 수신- orderId: {}", event.getOrderId());

        try {
            Order order = orderRepository.findById(event.getOrderId())
                                         .orElseThrow(() -> new RuntimeException("주문을 찾을 수 없음: " + event.getOrderId()));
            order.setStatus(OrderStatus.CANCELLED);
        } catch (Exception e) {
            log.error("결제 실패 처리 중 오류 - orderId: {}, error: {}", event.getOrderId(), e.getMessage());
        }
    }
}
