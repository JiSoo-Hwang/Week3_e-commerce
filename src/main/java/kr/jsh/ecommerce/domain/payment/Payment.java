package kr.jsh.ecommerce.domain.payment;

import jakarta.persistence.*;
import kr.jsh.ecommerce.domain.order.Order;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "payment")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id",nullable = false)
    private Order order;

    @Column(nullable = false)
    private int amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus paymentStatus;

    @Column(nullable = false)
    private LocalDateTime paidAt;

    // 기본 상태 "PENDING"을 적용하는 생성자 추가
    public static Payment create(Order order, int amount) {
        return new Payment(null, order, amount, PaymentStatus.PENDING, LocalDateTime.now());
    }

    // 상태 변경 메서드 추가
    public void completePayment() {
        this.paymentStatus = PaymentStatus.SUCCESS;
    }

    // 결제 실패 상태 변경
    public void failPayment() {
        this.paymentStatus = PaymentStatus.FAILED;
    }

    // 결제 환불
    public void refundPayment() {
        this.paymentStatus = PaymentStatus.REFUNDED;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}
