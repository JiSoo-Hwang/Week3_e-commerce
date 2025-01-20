package kr.jsh.ecommerce.domain.fruit;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QFruit is a Querydsl query type for Fruit
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QFruit extends EntityPathBase<Fruit> {

    private static final long serialVersionUID = 1627140308L;

    public static final QFruit fruit = new QFruit("fruit");

    public final kr.jsh.ecommerce.base.entity.QBaseEntity _super = new kr.jsh.ecommerce.base.entity.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> fruitId = createNumber("fruitId", Long.class);

    public final StringPath fruitName = createString("fruitName");

    public final NumberPath<Integer> fruitPrice = createNumber("fruitPrice", Integer.class);

    public final NumberPath<Integer> fruitStock = createNumber("fruitStock", Integer.class);

    public final ListPath<kr.jsh.ecommerce.domain.order.OrderFruit, kr.jsh.ecommerce.domain.order.QOrderFruit> orderFruits = this.<kr.jsh.ecommerce.domain.order.OrderFruit, kr.jsh.ecommerce.domain.order.QOrderFruit>createList("orderFruits", kr.jsh.ecommerce.domain.order.OrderFruit.class, kr.jsh.ecommerce.domain.order.QOrderFruit.class, PathInits.DIRECT2);

    public final StringPath status = createString("status");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QFruit(String variable) {
        super(Fruit.class, forVariable(variable));
    }

    public QFruit(Path<? extends Fruit> path) {
        super(path.getType(), path.getMetadata());
    }

    public QFruit(PathMetadata metadata) {
        super(Fruit.class, metadata);
    }

}

