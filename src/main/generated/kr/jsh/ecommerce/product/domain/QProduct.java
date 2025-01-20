package kr.jsh.ecommerce.product.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QProduct is a Querydsl query type for Product
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QProduct extends EntityPathBase<Product> {

    private static final long serialVersionUID = 255199208L;

    public static final QProduct product = new QProduct("product");

    public final StringPath brand = createString("brand");

    public final NumberPath<Long> productId = createNumber("productId", Long.class);

    public final StringPath productName = createString("productName");

    public final NumberPath<Integer> productPrice = createNumber("productPrice", Integer.class);

    public final ListPath<Stock, QStock> stock = this.<Stock, QStock>createList("stock", Stock.class, QStock.class, PathInits.DIRECT2);

    public QProduct(String variable) {
        super(Product.class, forVariable(variable));
    }

    public QProduct(Path<? extends Product> path) {
        super(path.getType(), path.getMetadata());
    }

    public QProduct(PathMetadata metadata) {
        super(Product.class, metadata);
    }

}

