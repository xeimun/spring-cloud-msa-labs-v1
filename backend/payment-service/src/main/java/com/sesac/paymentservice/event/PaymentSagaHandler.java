package com.sesac.paymentservice.event;

import com.sesac.paymentservice.entity.Payment;
import com.sesac.paymentservice.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentSagaHandler {

    private final PaymentService paymentService;
    private final PaymentSagaPublisher paymentSagaPublisher;

    @RabbitListener(queues = "${order.event.queue.payment-request}")
    public void handlePaymentRequest(PaymentRequestEvent event) {
        log.info("결제 요청 이벤트 수신 - orderId: {}, amount: {}", event.getOrderId(), event.getTotalAmount());

        // 결제 시도
        Payment payment = null;
        try {
            paymentService.processPayment(event);
            PaymentCompletedEvent paymentCompletedEvent = new PaymentCompletedEvent(
                    event.getOrderId(),
                    event.getUserId(),
                    event.getTotalAmount()
            );

            paymentSagaPublisher.publishPaymentCompleted(paymentCompletedEvent);
            log.info("결제 완료 이벤트 발행 완료 -> order-service");

        } catch (Exception e) {
            PaymentFailedEvent paymentFailedEvent = new PaymentFailedEvent(
                    event.getOrderId(),
                    event.getUserId(),
                    event.getProductId(),
                    event.getQuantity(),
                    e.getMessage()
            );
            paymentSagaPublisher.publishPaymentFailed(paymentFailedEvent);
            log.info("결제 실패 이벤트 발행 완료 -> order-service, product-service");
        }
    }
}
