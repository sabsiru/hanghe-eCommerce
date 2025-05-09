package kr.hhplus.be.server.application.coupon;

import jakarta.transaction.Transactional;
import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.coupon.CouponService;
import kr.hhplus.be.server.domain.coupon.UserCoupon;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Transactional
public class CouponFacade {

    private final CouponService couponService;

    public UserCoupon issueCoupon(Long userId, Long couponId) {
        return couponService.issueCoupon(userId, couponId);
    }

    public Coupon getCouponOrThrow(Long couponId) {
        return couponService.getCouponOrThrow(couponId);
    }
}