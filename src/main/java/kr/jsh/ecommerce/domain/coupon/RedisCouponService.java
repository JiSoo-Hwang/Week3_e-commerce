package kr.jsh.ecommerce.domain.coupon;

import kr.jsh.ecommerce.base.dto.BaseErrorCode;
import kr.jsh.ecommerce.base.exception.BaseCustomException;
import kr.jsh.ecommerce.domain.customer.Customer;
import kr.jsh.ecommerce.domain.customer.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class RedisCouponService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final CouponRepository couponRepository;
    private final CustomerRepository customerRepository;
    private final CouponIssueRepository couponIssueRepository;

    public boolean issueCoupon(Long couponId, Long customerId) {
        String stockKey = "coupon_stock:" + couponId; // 쿠폰 재고 관리용 Redis 키
        String issuedKey = "coupon_issued:" + couponId + ":" + customerId; // 중복 발급 방지용 Redis 키

        // Step 1: 중복 발급 여부 확인
        Boolean alreadyIssued = redisTemplate.hasKey(issuedKey);
        if (Boolean.TRUE.equals(alreadyIssued)) {
            return false; // 이미 발급된 쿠폰
        }

        // Step 2: Redis에서 쿠폰 재고 차감 (DECR 사용)
        Long stock = redisTemplate.opsForValue().decrement(stockKey);
        if (stock == null || stock < 0) {
            redisTemplate.opsForValue().increment(stockKey); // 롤백 (초과 발급 방지)
            return false;
        }

        // Step 3: 쿠폰 및 고객 정보 조회 (캐시 -> DB 조회)
        Coupon coupon = getCoupon(couponId); // 캐시에서 조회 후, 없으면 DB에서 가져와 캐싱
        Customer customer = getCustomer(customerId); // 캐시에서 조회 후, 없으면 DB에서 가져와 캐싱

        // Step 4: 쿠폰 발급 처리 (발급 가능 여부 확인 및 issuedCount 증가)
        coupon.issueCoupon(); // `issuedCount` 증가 (쿠폰 재고 관리)

        // Step 5: 쿠폰 발급 정보를 DB에 저장
        CouponIssue couponIssue = CouponIssue.create(coupon, customer);
        couponIssueRepository.save(couponIssue);

        // Step 6: Redis에 발급 정보 저장 (중복 방지 + TTL 설정)
        redisTemplate.opsForValue().set(issuedKey, "true", Duration.ofDays(1));

        return true; // 쿠폰 발급 성공
    }

    public Coupon getCoupon(Long couponId) {
        String key = "coupon:" + couponId;

        // Step 1: Redis에서 조회
        Coupon cachedCoupon = (Coupon) redisTemplate.opsForValue().get(key);
        if (cachedCoupon != null) {
            return cachedCoupon;
        }

        // Step 2: Redis에 없으면 DB에서 조회 후 캐싱
        Coupon coupon = couponRepository.findById(couponId)
                .orElseThrow(() -> new BaseCustomException(BaseErrorCode.NOT_FOUND, new String[]{String.valueOf(couponId)}));
        redisTemplate.opsForValue().set(key, coupon, Duration.ofHours(1));

        return coupon;
    }

    public Customer getCustomer(Long customerId) {
        String key = "customer:" + customerId;

        // Step 1: Redis에서 조회
        Customer cachedCustomer = (Customer) redisTemplate.opsForValue().get(key);
        if (cachedCustomer != null) {
            return cachedCustomer;
        }

        // Step 2: Redis에 없으면 DB에서 조회 후 캐싱
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new BaseCustomException(BaseErrorCode.NOT_FOUND, new String[]{String.valueOf(customerId)}));
        redisTemplate.opsForValue().set(key, customer, Duration.ofHours(1));

        return customer;
    }
}
