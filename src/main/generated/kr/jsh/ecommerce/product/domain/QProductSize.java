package kr.jsh.ecommerce.product.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QProductSize is a Querydsl query type for ProductSize
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QProductSize extends EntityPathBase<ProductSize> {

    private static final long serialVersionUID = -205051895L;

    public static final QProductSize productSize1 = new QProductSize("productSize1");

    public final NumberPath<Integer> productSize = createNumber("productSize", Integer.class);

    public final NumberPath<Long> sizeId = createNumber("sizeId", Long.class);

    public QProductSize(String variable) {
        super(ProductSize.class, forVariable(variable));
    }

    public QProductSize(Path<? extends ProductSize> path) {
        super(path.getType(), path.getMetadata());
    }

    public QProductSize(PathMetadata metadata) {
        super(ProductSize.class, metadata);
    }

}

