package com.sesac.paymentservice.service;

import com.sesac.paymentservice.entity.Payment;
import com.sesac.paymentservice.entity.PaymentStatus;
import com.sesac.paymentservice.event.PaymentRequestEvent;
import com.sesac.paymentservice.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class PaymentService {
    private final PaymentRepository paymentRepository;

    // 결제 로직
    public Payment processPayment(PaymentRequestEvent event) {

        Payment payment = new Payment();
        payment.setOrderId(event.getOrderId());
        payment.setUserId(event.getUserId());
        payment.setAmount(event.getTotalAmount());
        payment.setStatus(PaymentStatus.PENDING);
        payment.setPaymentMethod("CREDIT");

        Payment savedPayment = paymentRepository.save(payment);

        // 결제 처리
        try {
            Thread.sleep(2000);

            if (Math.random() < 0.3) {
                throw new RuntimeException("잔액 부족");
            }
            payment.setStatus(PaymentStatus.COMPLETED);
            paymentRepository.save(savedPayment);

        } catch (Exception e) {
            payment.setStatus(PaymentStatus.FAILED);
            savedPayment.setFailureReason(e.getMessage());
            paymentRepository.save(savedPayment);
            throw new RuntimeException("결제 실패");
        }

        return savedPayment;
    }
}
