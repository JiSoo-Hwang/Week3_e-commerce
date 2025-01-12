package kr.jsh.ecommerce.order.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QOrderSheet is a Querydsl query type for OrderSheet
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QOrderSheet extends EntityPathBase<OrderSheet> {

    private static final long serialVersionUID = 1909675191L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QOrderSheet orderSheet = new QOrderSheet("orderSheet");

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final QOrder order;

    public final NumberPath<Long> orderSheetId = createNumber("orderSheetId", Long.class);

    public QOrderSheet(String variable) {
        this(OrderSheet.class, forVariable(variable), INITS);
    }

    public QOrderSheet(Path<? extends OrderSheet> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QOrderSheet(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QOrderSheet(PathMetadata metadata, PathInits inits) {
        this(OrderSheet.class, metadata, inits);
    }

    public QOrderSheet(Class<? extends OrderSheet> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.order = inits.isInitialized("order") ? new QOrder(forProperty("order"), inits.get("order")) : null;
    }

}

