package kr.jsh.ecommerce.domain.coupon;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCouponIssue is a Querydsl query type for CouponIssue
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCouponIssue extends EntityPathBase<CouponIssue> {

    private static final long serialVersionUID = -1304996367L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QCouponIssue couponIssue = new QCouponIssue("couponIssue");

    public final QCoupon coupon;

    public final NumberPath<Long> couponIssueId = createNumber("couponIssueId", Long.class);

    public final kr.jsh.ecommerce.domain.customer.QCustomer customer;

    public final DateTimePath<java.time.LocalDateTime> expiredAt = createDateTime("expiredAt", java.time.LocalDateTime.class);

    public final DateTimePath<java.time.LocalDateTime> issuedAt = createDateTime("issuedAt", java.time.LocalDateTime.class);

    public final EnumPath<CouponStatus> status = createEnum("status", CouponStatus.class);

    public final DateTimePath<java.time.LocalDateTime> usedAt = createDateTime("usedAt", java.time.LocalDateTime.class);

    public QCouponIssue(String variable) {
        this(CouponIssue.class, forVariable(variable), INITS);
    }

    public QCouponIssue(Path<? extends CouponIssue> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QCouponIssue(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QCouponIssue(PathMetadata metadata, PathInits inits) {
        this(CouponIssue.class, metadata, inits);
    }

    public QCouponIssue(Class<? extends CouponIssue> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.coupon = inits.isInitialized("coupon") ? new QCoupon(forProperty("coupon")) : null;
        this.customer = inits.isInitialized("customer") ? new kr.jsh.ecommerce.domain.customer.QCustomer(forProperty("customer"), inits.get("customer")) : null;
    }

}

