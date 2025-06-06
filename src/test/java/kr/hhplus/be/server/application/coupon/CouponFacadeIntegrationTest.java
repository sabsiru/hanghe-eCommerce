package kr.hhplus.be.server.application.coupon;

import kr.hhplus.be.server.domain.coupon.*;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;
import static org.junit.Assert.assertThrows;

@SpringBootTest
class CouponFacadeIntegrationTest {

    @Autowired
    private CouponFacade couponFacade;

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private UserCouponRepository userCouponRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CouponService couponService;

    @Test
    void 쿠폰_정상_발급() {
        User user = userRepository.save(User.create("사용자", 0));

        Coupon coupon = couponService.create("10% 할인", 10, 3000, LocalDateTime.now().plusDays(3), 100);

        UserCoupon issued = couponFacade.issue(user.getId(), coupon.getId());

        assertThat(issued.getUserId()).isEqualTo(user.getId());
        assertThat(issued.getCouponId()).isEqualTo(coupon.getId());
        assertThat(issued.getStatus()).isEqualTo(UserCouponStatus.ISSUED);
    }

    @Test
    void 쿠폰_발급_수량_초과시_예외() {
        User user1 = userRepository.save(User.create("사용자1", 0));
        User user2 = userRepository.save(User.create("사용자2", 0));

        Coupon coupon = couponService.create("한정쿠폰", 50, 5000, LocalDateTime.now().plusDays(3), 1);

        couponFacade.issue(user1.getId(), coupon.getId());

        assertThatThrownBy(() -> couponFacade.issue(user2.getId(), coupon.getId()))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("발급이 종료된 쿠폰입니다.");
    }

    @Test
    void 만료된_쿠폰_발급시_예외() {
        User user = userRepository.save(User.create("사용자", 0));
        Coupon coupon = couponService.create("만료쿠폰", 10, 2000, LocalDateTime.now().minusDays(1), 10);

        assertThatThrownBy(() -> couponFacade.issue(user.getId(), coupon.getId()))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("발급이 종료된 쿠폰입니다.");
    }

    @Test
    void 쿠폰_단건_조회() {
        Coupon coupon = couponService.create("단건조회", 15, 3000, LocalDateTime.now().plusDays(3), 20);

        Coupon found = couponFacade.getCouponOrThrow(coupon.getId());

        assertThat(found).isNotNull();
        assertThat(found.getId()).isEqualTo(coupon.getId());
        assertThat(found.getName()).isEqualTo("단건조회");
    }

    @Test
    void 쿠폰_발급_성공_후_발급불가_예외() {
        // given
        User user1 = userRepository.save(User.create("사용자1", 0));
        User user2 = userRepository.save(User.create("사용자2", 0));

        Coupon coupon = couponService.create("단일발급쿠폰", 10, 5000, LocalDateTime.now().plusDays(1), 1);

        // when
        UserCoupon issuedCoupon = couponFacade.issue(user1.getId(), coupon.getId());
        assertThat(issuedCoupon.getUserId()).isEqualTo(user1.getId());

        // then
        IllegalStateException exception = assertThrows(IllegalStateException.class, () ->
                couponFacade.issue(user2.getId(), coupon.getId())
        );

        assertThat(exception.getMessage()).contains("발급이 종료된 쿠폰입니다.");
    }
}
