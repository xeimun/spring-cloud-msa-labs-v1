package com.sesac.productservice.event;

import java.io.Serializable;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequestEvent implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long orderId;
    private Long userId;
    private Long productId;
    private Integer quantity;
    private BigDecimal totalAmount;
}
