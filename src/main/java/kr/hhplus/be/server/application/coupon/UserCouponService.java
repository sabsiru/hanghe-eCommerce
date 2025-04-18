package kr.hhplus.be.server.application.coupon;

import kr.hhplus.be.server.domain.coupon.UserCoupon;
import kr.hhplus.be.server.domain.coupon.UserCouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserCouponService {

    private final UserCouponRepository userCouponRepository;

    /**
     * 사용자 쿠폰 저장
     */
    public UserCoupon save(UserCoupon userCoupon) {
        return userCouponRepository.save(userCoupon);
    }

    /**
     * ID로 사용자 쿠폰 조회 (없으면 예외)
     */
    public UserCoupon getById(Long userCouponId) {
        return userCouponRepository.findById(userCouponId)
                .orElseThrow(() -> new IllegalArgumentException("사용자 쿠폰을 찾을 수 없습니다. userCouponId=" + userCouponId));
    }

    /*
    * 사용자 id로 쿠폰 조회
    * */
    public List<UserCoupon> findByUserId(Long userId) {
      return userCouponRepository.findAllByUserId(userId);
    }

    /**
     * 쿠폰 사용 처리
     */
    public UserCoupon useCoupon(Long userCouponId) {
        UserCoupon userCoupon = getById(userCouponId);
        UserCoupon updated = userCoupon.use();
        return userCouponRepository.save(updated);
    }

    /**
     * 사용자 쿠폰 환불 처리
     */
    public UserCoupon refundCoupon(Long userCouponId) {
        UserCoupon userCoupon = getById(userCouponId);
        UserCoupon updated = userCoupon.refund();
        return userCouponRepository.save(updated);
    }

    /**
     * 사용자 ID와 쿠폰 ID로 사용자 쿠폰 중복 검증
     */
    public void validateNotDuplicated(Long userId, Long couponId) {
        Optional<UserCoupon> existing = userCouponRepository.findByUserIdAndCouponId(userId, couponId);
        if (existing.isPresent()) {
            throw new IllegalStateException("이미 발급받은 쿠폰입니다.");
        }
    }
}