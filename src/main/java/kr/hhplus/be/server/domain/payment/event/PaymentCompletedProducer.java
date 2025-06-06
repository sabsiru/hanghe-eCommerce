package kr.hhplus.be.server.domain.payment.event;

import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.payment.Payment;

public interface PaymentCompletedProducer {
    void send(Payment payment, Order order);
}
