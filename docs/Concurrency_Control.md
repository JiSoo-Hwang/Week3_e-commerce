# 동시성 제어 보고서

## 1. 서론
### 1.1 프로젝트 개요
- **프로젝트 주제**: 이커머스 시스템의 핵심 기능 구현
- **핵심 기능**:
    1. 상품 재고 차감 및 복원
       - **Entity**:
         - [Customer.java](https://github.com/JiSoo-Hwang/Week3_e-commerce/blob/main/src/main/java/kr/jsh/ecommerce/domain/customer/Customer.java#L9-L36)
         - [Order.java](https://github.com/JiSoo-Hwang/Week3_e-commerce/blob/main/src/main/java/kr/jsh/ecommerce/domain/order/Order.java#L12-L69)
         - [OrderFruit.java](https://github.com/JiSoo-Hwang/Week3_e-commerce/blob/main/src/main/java/kr/jsh/ecommerce/domain/order/OrderFruit.java#L11-L51)
       
       - **DTO**:
         - [OrderCreateRequest.java](https://github.com/JiSoo-Hwang/Week3_e-commerce/blob/main/src/main/java/kr/jsh/ecommerce/interfaces/api/order/dto/OrderCreateRequest.java#L5-L9)
         - [OrderCreateResponse.java](https://github.com/JiSoo-Hwang/Week3_e-commerce/blob/main/src/main/java/kr/jsh/ecommerce/interfaces/api/order/dto/OrderCreateResponse.java#L10-L30)
         - [OrderFruitRequest.java](https://github.com/JiSoo-Hwang/Week3_e-commerce/blob/main/src/main/java/kr/jsh/ecommerce/interfaces/api/order/dto/OrderFruitRequest.java#L3-L9)
       
       - **Controller**:
         - [OrderController.java](https://github.com/JiSoo-Hwang/Week3_e-commerce/blob/main/src/main/java/kr/jsh/ecommerce/interfaces/api/order/controller/OrderController.java#L18-L36)
       
       - **Facade/Service**:
         - [CreateOrderUseCase.java](https://github.com/JiSoo-Hwang/Week3_e-commerce/blob/main/src/main/java/kr/jsh/ecommerce/application/order/CreateOrderUseCase.java#L16-L31)
         - [CustomerService.java](https://github.com/JiSoo-Hwang/Week3_e-commerce/blob/main/src/main/java/kr/jsh/ecommerce/domain/customer/CustomerService.java#L9-L20)
         - [OrderService.java](https://github.com/JiSoo-Hwang/Week3_e-commerce/blob/main/src/main/java/kr/jsh/ecommerce/domain/order/OrderService.java#L24-L59)
       
       - **Repository**:
         - [OrderRepository.java](https://github.com/JiSoo-Hwang/Week3_e-commerce/blob/main/src/main/java/kr/jsh/ecommerce/domain/order/OrderRepository.java#L5-L7)
         - [OrderRepositoryImpl.java](https://github.com/JiSoo-Hwang/Week3_e-commerce/blob/main/src/main/java/kr/jsh/ecommerce/infrastructure/order/OrderRepositoryImpl.java#L10-L25)
         - [OrderJpaRepository.java](https://github.com/JiSoo-Hwang/Week3_e-commerce/blob/main/src/main/java/kr/jsh/ecommerce/infrastructure/order/OrderJpaRepository.java#L6-L7)

    2. 고객 지갑 잔액 충전 및 사용
       - **Entity**:
         - [Customer.java](https://github.com/JiSoo-Hwang/Week3_e-commerce/blob/main/src/main/java/kr/jsh/ecommerce/domain/customer/Customer.java#L9-L36)
         - [Wallet.java](https://github.com/JiSoo-Hwang/Week3_e-commerce/blob/main/src/main/java/kr/jsh/ecommerce/domain/wallet/Wallet.java#L9-L47)

       - **DTO**:
           - [ChargeWalletRequest.java](https://github.com/JiSoo-Hwang/Week3_e-commerce/blob/main/src/main/java/kr/jsh/ecommerce/interfaces/api/wallet/dto/ChargeWalletRequest.java#L3-L7)
           - [ChargeWalletResponse.java](https://github.com/JiSoo-Hwang/Week3_e-commerce/blob/main/src/main/java/kr/jsh/ecommerce/interfaces/api/wallet/dto/ChargeWalletResponse.java#L3-L6)

       - **Controller**:
           - [WalletController.java](https://github.com/JiSoo-Hwang/Week3_e-commerce/blob/main/src/main/java/kr/jsh/ecommerce/interfaces/api/wallet/controller/WalletController.java#L17-L45)

       - **Facade/Service**:
           - [ChargeWalletUseCase.java](https://github.com/JiSoo-Hwang/Week3_e-commerce/blob/main/src/main/java/kr/jsh/ecommerce/application/wallet/ChargeWalletUseCase.java#L10-L20)
           - [WalletService.java](https://github.com/JiSoo-Hwang/Week3_e-commerce/blob/main/src/main/java/kr/jsh/ecommerce/domain/wallet/WalletService.java#L11-L41)

       - **Repository**:
           - [WalletRepository.java](https://github.com/JiSoo-Hwang/Week3_e-commerce/blob/main/src/main/java/kr/jsh/ecommerce/domain/wallet/WalletRepository.java#L5-L8)
           - [WalletRepositoryImpl.java](https://github.com/JiSoo-Hwang/Week3_e-commerce/blob/main/src/main/java/kr/jsh/ecommerce/infrastructure/wallet/WalletRepositoryImpl.java#L10-L24)
           - [WalletJpaRepository.java](https://github.com/JiSoo-Hwang/Week3_e-commerce/blob/main/src/main/java/kr/jsh/ecommerce/infrastructure/wallet/WalletJpaRepository.java#L10-L14)

    3. 선착순 쿠폰 발급
       - **Entity**:
         - [Coupon.java](https://github.com/JiSoo-Hwang/Week3_e-commerce/blob/main/src/main/java/kr/jsh/ecommerce/domain/coupon/Coupon.java#L12-L40)
         - [CouponIssue.java](https://github.com/JiSoo-Hwang/Week3_e-commerce/blob/main/src/main/java/kr/jsh/ecommerce/domain/coupon/CouponIssue.java#L15-L73)
         - [Customer.java](https://github.com/JiSoo-Hwang/Week3_e-commerce/blob/main/src/main/java/kr/jsh/ecommerce/domain/customer/Customer.java#L9-L36)

       - **DTO**:
           - [CouponIssueRequest.java](https://github.com/JiSoo-Hwang/Week3_e-commerce/blob/main/src/main/java/kr/jsh/ecommerce/interfaces/api/coupon/dto/CouponIssueRequest.java#L6-L10)
           - [CouponIssueResponse.java](https://github.com/JiSoo-Hwang/Week3_e-commerce/blob/main/src/main/java/kr/jsh/ecommerce/interfaces/api/coupon/dto/CouponIssueResponse.java#L7-L32)

       - **Controller**:
           - [CouponController.java](https://github.com/JiSoo-Hwang/Week3_e-commerce/blob/main/src/main/java/kr/jsh/ecommerce/interfaces/api/coupon/controller/CouponController.java#L20-L55)

       - **Facade/Service**:
           - [IssueCouponUseCase.java](https://github.com/JiSoo-Hwang/Week3_e-commerce/blob/main/src/main/java/kr/jsh/ecommerce/application/coupon/IssueCouponUseCase.java#L17-L39)
           - [CouponService.java](https://github.com/JiSoo-Hwang/Week3_e-commerce/blob/main/src/main/java/kr/jsh/ecommerce/domain/coupon/CouponService.java#L13-L29)
           - [CouponIssueService.java](https://github.com/JiSoo-Hwang/Week3_e-commerce/blob/main/src/main/java/kr/jsh/ecommerce/domain/coupon/CouponIssueService.java#L12-L31)

       - **Repository**:
           - [CouponRepository.java](https://github.com/JiSoo-Hwang/Week3_e-commerce/blob/main/src/main/java/kr/jsh/ecommerce/domain/coupon/CouponRepository.java#L5-L9)
           - [CouponIssueRepository.java](https://github.com/JiSoo-Hwang/Week3_e-commerce/blob/main/src/main/java/kr/jsh/ecommerce/domain/coupon/CouponIssueRepository.java#L10-L13)
           - [CouponRepositoryImpl.java](https://github.com/JiSoo-Hwang/Week3_e-commerce/blob/main/src/main/java/kr/jsh/ecommerce/infrastructure/coupon/CouponRepositoryImpl.java#L10-L30)
           - [CouponIssueRepositoryImpl.java](https://github.com/JiSoo-Hwang/Week3_e-commerce/blob/main/src/main/java/kr/jsh/ecommerce/infrastructure/coupon/CouponIssueRepositoryImpl.java#L15-L30)
           - [CouponJpaRepository.java](https://github.com/JiSoo-Hwang/Week3_e-commerce/blob/main/src/main/java/kr/jsh/ecommerce/infrastructure/coupon/CouponJpaRepository.java#L12-L16)
           - [CouponIssueJpaRepository.java](https://github.com/JiSoo-Hwang/Week3_e-commerce/blob/main/src/main/java/kr/jsh/ecommerce/infrastructure/coupon/CouponIssueJpaRepository.java#L12-L14)


### 1.2 동시성 제어의 필요성
- **시나리오에서 발생할 수 있는 문제**:
    - **상품 재고 차감**: 동시 주문으로 인해 재고가 초과 차감될 위험성이 있습니다.
    - **잔액 충전**: 다중 요청 시 잔액이 잘못 갱신될 가능성이 있습니다.
    - **선착순 쿠폰 발급**: 한정된 수량의 쿠폰이 초과 발급되는 문제가 발생할 수 있습니다.
- **동시성 제어 목표**:
    - 데이터 무결성 보장
    - 시스템 성능 최적화

---

## 2. 기능별 동시성 이슈와 제어 방식

### 2.1 상품 재고 차감 및 복원
#### 2.1.1 발생 가능한 동시성 문제
- **Lost Update (분실 갱신)**:
    - 두 개 이상의 트랜잭션이 동일 상품의 재고를 동시에 차감하여 이전 상태를 덮어쓸 위험이 있습니다.

#### 2.1.2 적용된 동시성 제어 방식
- **비관적 락 (Pessimistic Lock)**:
    - `@Lock(LockModeType.PESSIMISTIC_WRITE)`를 사용하여 상품 재고 조회 시 락을 걸어 다른 트랜잭션에서 재고를 수정하지 못하도록 제한했습니다.

#### 2.1.3 장단점
- **장점**:
    - 강력한 동시성 제어로 데이터 일관성을 보장합니다.
    - 충돌 가능성이 높은 상황에서도 안전한 작업 수행할 수 있습니다.

- **단점**:
    - "트랜잭션 대기 시간이 증가하여 성능 저하 가능성이 있다."
        - 흔히 단점으로 언급되는 부분이지만, **실제로 데이터베이스 수준에서의 비관적 락은 낙관적 락에 비해 효율적일 수 있습니다**.
        - **예시: 선착순 쿠폰 발행 시나리오**:
            - 비관적 락의 경우:
                - 한 트랜잭션이 락을 획득하면, 트랜잭션이 완료(커밋)된 후 다음 트랜잭션이 순차적으로 진행됩니다.
            - 낙관적 락의 경우:
                - 한 트랜잭션이 성공적으로 커밋되는 동안 다른 트랜잭션은 충돌(예: `OptimisticLockException`)이 발생해 재시도를 반복해야 합니다.
                - **대기 중인 트랜잭션이 많을수록 실패와 재시도로 인해 오히려 성능이 저하**될 가능성이 있습니다.
        - 따라서, 충돌이 빈번하거나 트랜잭션 요청이 많은 환경에서는 **비관적 락이 더 적합**합니다.
    - **데드락(Deadlock) 위험** 존재:
        - 여러 트랜잭션이 동일한 자원을 동시에 대기하면서 순환 대기 상태가 발생할 수 있습니다.
        - 자원 접근 순서를 명확히 설계하고, 락 타임아웃을 설정해 데드락 발생 가능성을 줄여야 합니다.

#### 2.1.4 구현 복잡도 및 효율성
- **복잡도**: 락 설정과 트랜잭션 범위 관리가 필요해보입니다.
- **효율성**: 충돌이 빈번한 환경에서는 적합하지만, 여전히 락으로 인한 성능 저하를 고려해야 합니다.

---

### 2.2 고객 지갑 잔액 충전 및 사용
#### 2.2.1 발생 가능한 동시성 문제
- **Lost Update (분실 갱신)**:
    - 다중 충전 요청으로 인해 잔액이 잘못 갱신됨.

#### 2.2.2 적용된 동시성 제어 방식
- **낙관적 락 (Optimistic Lock)**:
    - `@Version` 필드를 사용하여 데이터 충돌을 감지하고, 충돌 시 예외를 발생시켜 재시도 로직으로 처리합니다.

#### 2.2.3 장단점
- **장점**:
    - 데이터 접근 병렬성을 증가시킬 수 있습니다.
    - 충돌이 드문 환경에서 성능을 최적화할 수 있습니다.
- **단점**:
    - 충돌 발생 시 재시도 로직 구현이 필요합니다.
    - 충돌이 잦은 환경에서는 성능 저하 가능성이 있습니다.

#### 2.2.4 구현 복잡도 및 효율성
- **복잡도**: 비교적 높음 (충돌 처리와 재시도 로직을 추가할 필요가 있습니다).
- **효율성**: 충돌이 드문 환경에서는 높은 효율성을 보입니다.

---

### 2.3 선착순 쿠폰 발급
#### 2.3.1 발생 가능한 동시성 문제
- **Over-Issued Coupons**:
    - 다수의 요청이 동시에 처리되며 제한된 수량을 초과하여 쿠폰이 발급될 수 있습니다.

#### 2.3.2 적용된 동시성 제어 방식
- **비관적 락 (Pessimistic Lock)**:
    - `@Lock(LockModeType.PESSIMISTIC_WRITE)`로 쿠폰 조회 시 락을 걸어 초과 발급을 방지하는 설정을 했습니다.

#### 2.3.3 장단점
- **장점**:
    - 초과 발급 문제를 방지할 수 있습니다.
- **단점**:
    - 락으로 인한 트랜잭션 대기 시간이 증가됩니다.
    - 쿠폰 발급 요청이 많은 경우, 같은 케이스에 낙관적 락을 적용하는 것보다는 성능이 좋을 수 있지만, 단일 DB에서는 여전히 성능 이슈가 있습니다.

#### 2.3.4 구현 복잡도 및 효율성
- **복잡도**: 낮음 (비관적 락 적용이 비교적 단순합니다).
- **효율성**: 강력한 동시성 제어로 데이터 무결성을 보장할 수 있습니다.

---

## 3. 테스트 코드 및 성능 분석

### 3.1 동시성 테스트 코드
#### 3.1.1 상품 재고 차감
- **테스트 목표**: 다중 트랜잭션이 재고를 동시에 차감할 때 데이터 무결성 확인.
#### 3.1.2 잔액 충전
- **테스트 목표**: 낙관적 락 적용 시 충돌 감지 및 재시도 로직 검증.
#### 3.1.3 선착순 쿠폰 발급
- **테스트 목표**: 다중 요청 시 초과 발급 방지 확인.

---

## 4. 결론 및 개선 방향
### 4.1 동시성 제어 방식 요약
- 비관적 락은 강력한 제어를 제공하지만 요청이 많으면 성능에 영향을 미칠 수 있습니다.
- 낙관적 락은 병렬성을 높일 수 있지만 충돌이 잦은 환경에서는 재시도가 늘어나기 때문에 적합하지 않습니다.

### 4.2 향후 개선 방향
- 트랜잭션 범위를 최소화하여 락 유지 시간을 줄임(이를 염두에 두고 리팩토링을 하겠습니다).
- 분산 락(Redis 등) 도입을 검토하여 확장성 향상(step 12 과제에 적용해보겠습니다).