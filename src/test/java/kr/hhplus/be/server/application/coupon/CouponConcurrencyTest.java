package kr.hhplus.be.server.application.coupon;

import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.coupon.CouponRepository;
import kr.hhplus.be.server.domain.coupon.UserCoupon;
import kr.hhplus.be.server.domain.coupon.UserCouponRepository;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class CouponConcurrencyTest {

    @Autowired
    private CouponFacade couponFacade;

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private UserCouponRepository userCouponRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void 여러유저가_동시_쿠폰_발급시_1건발급_테스트() throws InterruptedException {
        // given
        Coupon coupon = couponRepository.save(Coupon.create("테스트쿠폰", 10, 1000, LocalDateTime.now().plusDays(1), 1));

        int threadCount = 10;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            User user = userRepository.save(User.create("user" + i, 0));
            long userId = user.getId();

            executorService.submit(() -> {
                try {
                    couponFacade.issueCoupon(userId, coupon.getId());
                } catch (Exception e) {
                    // 무시
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        executorService.shutdown();
        executorService.awaitTermination(1, TimeUnit.SECONDS);
        // then
        List<UserCoupon> allCoupons = userCouponRepository.findAll();
        assertThat(allCoupons).hasSize(1); // 최대 1개까지만 발급돼야 함
    }

    @Test
    void 동시_쿠폰_발급_시_한_유저에게_중복_발급_제한됨() throws Exception {
        // given
        User user = userRepository.save(User.create("중복테스트유저", 0));
        Coupon coupon = couponRepository.save(Coupon.create("단일쿠폰", 10, 5000,
                LocalDateTime.now().plusDays(1), 10)); // 충분한 수량

        int threadCount = 10;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        // when
        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    couponFacade.issueCoupon(user.getId(), coupon.getId());
                } catch (Exception ignored) {
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        executorService.shutdown();
        executorService.awaitTermination(1, TimeUnit.SECONDS);
        // then
        List<UserCoupon> results = userCouponRepository.findAllByUserId(user.getId());
        assertThat(results).hasSize(1);
    }
}