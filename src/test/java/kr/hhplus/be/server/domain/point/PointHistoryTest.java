package kr.hhplus.be.server.domain.point;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class PointHistoryTest {

    @Test
    void 충전_내역을_정상적으로_생성할_수_있다() {
        // given
        Long userId = 1L;
        long amount = 50_000;

        // when
        PointHistory history = PointHistory.charge(userId, amount);

        // then
        assertThat(history.getUserId()).isEqualTo(userId);
        assertThat(history.getAmount()).isEqualTo(amount);
        assertThat(history.getType()).isEqualTo(PointHistoryType.CHARGE);
        assertThat(history.getOrderId()).isNull();
    }

    @Test
    void 사용_내역을_정상적으로_생성할_수_있다() {
        // given
        Long userId = 1L;
        long amount = 30_000;

        // when
        PointHistory history = PointHistory.use(userId, amount);

        // then
        assertThat(history.getUserId()).isEqualTo(userId);
        assertThat(history.getAmount()).isEqualTo(amount);
        assertThat(history.getType()).isEqualTo(PointHistoryType.USE);
        assertThat(history.getOrderId()).isNull();
    }

    @Test
    void 환불_내역을_정상적으로_생성할_수_있다() {
        // given
        Long userId = 1L;
        long amount = 20_000;
        Long orderId = 100L;

        // when
        PointHistory history = PointHistory.refund(userId, amount, orderId);

        // then
        assertThat(history.getUserId()).isEqualTo(userId);
        assertThat(history.getAmount()).isEqualTo(amount);
        assertThat(history.getType()).isEqualTo(PointHistoryType.REFUND);
        assertThat(history.getOrderId()).isEqualTo(orderId);
    }
}