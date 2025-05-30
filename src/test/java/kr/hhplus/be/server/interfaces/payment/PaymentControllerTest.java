package kr.hhplus.be.server.interfaces.payment;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.hhplus.be.server.application.payment.PaymentFacade;
import kr.hhplus.be.server.domain.payment.Payment;
import kr.hhplus.be.server.domain.payment.PaymentStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PaymentController.class)
class PaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PaymentFacade paymentFacade;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void 결제_성공_쿠폰없을시() throws Exception {
        // given
        Long orderId = 1L;
        PaymentRequest request = new PaymentRequest(10000);

        Payment payment = Payment.builder()
                .id(1L)
                .orderId(orderId)
                .amount(10000)
                .status(PaymentStatus.COMPLETED)
                .couponId(null)
                .build();
        when(paymentFacade.processPayment(eq(orderId), eq(10000)))
                .thenReturn(payment);

        // when & then
        mockMvc.perform(patch("/payments/{orderId}/pay", orderId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.orderId").value(orderId))
                .andExpect(jsonPath("$.amount").value(10000))
                .andExpect(jsonPath("$.status").value("COMPLETED"))
                .andExpect(jsonPath("$.couponId").isEmpty());
    }

    @Test
    void 결제_성공_쿠폰이있을시() throws Exception {
        // given
        Long orderId = 1L;
        PaymentRequest request = new PaymentRequest(15000);

        Long couponId = 500L;

        // 20% 할인 쿠폰이지만 최대 할인액 2,000원 적용: 15000 * 0.2 = 3000이 계산되나, 최대 2,000원으로 제한.
        int finalPaymentAmount = 15000 - 2000; // 13000

        Payment payment = Payment.builder()
                .id(1L)
                .orderId(orderId)
                .amount(finalPaymentAmount)
                .status(PaymentStatus.COMPLETED)
                .couponId(couponId)
                .build();
        when(paymentFacade.processPayment(eq(orderId), eq(15000)))
                .thenReturn(payment);

        // when & then
        mockMvc.perform(patch("/payments/{orderId}/pay", orderId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.orderId").value(orderId))
                .andExpect(jsonPath("$.amount").value(finalPaymentAmount))
                .andExpect(jsonPath("$.status").value("COMPLETED"))
                .andExpect(jsonPath("$.couponId").value(couponId));
    }

    @Test
    void 결제_환불_성공_쿠폰없을때() throws Exception {
        // given
        long orderId = 1L;
        Payment refundPayment = Payment.builder()
                .id(1L)
                .orderId(orderId)
                .amount(10000)
                .status(PaymentStatus.REFUND)
                .couponId(null)
                .build();

        when(paymentFacade.processRefund(orderId)).thenReturn(refundPayment);

        // when & then
        mockMvc.perform(patch("/payments/{orderId}/refund", orderId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(refundPayment.getId()))
                .andExpect(jsonPath("$.orderId").value(orderId))
                .andExpect(jsonPath("$.amount").value(refundPayment.getAmount()))
                .andExpect(jsonPath("$.status").value("REFUND"))
                .andExpect(jsonPath("$.couponId").doesNotExist());
    }

    @Test
    void 결제_환불_성공_쿠폰있을때() throws Exception {
        // given
        long orderId = 1L;
        Long couponId = 500L;

        Payment refundPayment = Payment.builder()
                .id(1L)
                .orderId(orderId)
                .amount(25000)
                .status(PaymentStatus.REFUND)
                .couponId(couponId)
                .build();
        when(paymentFacade.processRefund(eq(orderId))).thenReturn(refundPayment);

        // when & then
        mockMvc.perform(patch("/payments/{orderId}/refund", orderId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(refundPayment.getId()))
                .andExpect(jsonPath("$.orderId").value(orderId))
                .andExpect(jsonPath("$.amount").value(refundPayment.getAmount()))
                .andExpect(jsonPath("$.status").value("REFUND"))
                .andExpect(jsonPath("$.couponId").value(couponId));
    }

    @Test
    void 사용자_포인트_부족으로_결제_실패() throws Exception {
        // given
        Long orderId = 1L;
        int paymentAmount = 50000;

        String requestBody = "{\"paymentAmount\": " + 50000 + "}";
        doThrow(new IllegalStateException("결제 실패: 재고 부족 또는 포인트 부족"))
                .when(paymentFacade).processPayment(orderId, paymentAmount);

        // when & then
        mockMvc.perform(patch("/payments/{orderId}/pay", orderId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("결제 실패: 재고 부족 또는 포인트 부족")));
    }

    @Test
    void 결제_실패_예외() throws Exception {
        // given
        Long orderId = 1L;
        int invalidAmount = 0;

        doThrow(new IllegalArgumentException("포인트는 0보다 커야 합니다."))
                .when(paymentFacade).processPayment(orderId, invalidAmount);

        // when & then
        mockMvc.perform(patch("/payments/{orderId}/pay", orderId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"paymentAmount\": 0}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("포인트는 0보다 커야 합니다.")));
    }

    @Test
    void 환불_실패() throws Exception {
        // given
        Long orderId = 1L;

        doThrow(new IllegalStateException("환불 실패: 결제 정보가 없습니다."))
                .when(paymentFacade).processRefund(orderId);

        // when & then
        mockMvc.perform(patch("/payments/{orderId}/refund", orderId))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("환불 실패: 결제 정보가 없습니다.")));
    }
}