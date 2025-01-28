package kr.jsh.ecommerce.domain.order;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QOrderFruit is a Querydsl query type for OrderFruit
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QOrderFruit extends EntityPathBase<OrderFruit> {

    private static final long serialVersionUID = 831167788L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QOrderFruit orderFruit = new QOrderFruit("orderFruit");

    public final kr.jsh.ecommerce.domain.fruit.QFruit fruit;

    public final NumberPath<Integer> fruitPrice = createNumber("fruitPrice", Integer.class);

    public final QOrder order;

    public final NumberPath<Long> orderFruitId = createNumber("orderFruitId", Long.class);

    public final NumberPath<Integer> quantity = createNumber("quantity", Integer.class);

    public final NumberPath<Integer> subTotal = createNumber("subTotal", Integer.class);

    public QOrderFruit(String variable) {
        this(OrderFruit.class, forVariable(variable), INITS);
    }

    public QOrderFruit(Path<? extends OrderFruit> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QOrderFruit(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QOrderFruit(PathMetadata metadata, PathInits inits) {
        this(OrderFruit.class, metadata, inits);
    }

    public QOrderFruit(Class<? extends OrderFruit> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.fruit = inits.isInitialized("fruit") ? new kr.jsh.ecommerce.domain.fruit.QFruit(forProperty("fruit")) : null;
        this.order = inits.isInitialized("order") ? new QOrder(forProperty("order"), inits.get("order")) : null;
    }

}

