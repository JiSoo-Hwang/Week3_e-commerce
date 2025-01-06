package kr.jsh.ecommerce.product.infrastructure;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import kr.jsh.ecommerce.product.domain.Product;
import kr.jsh.ecommerce.product.domain.ProductStatus;
import kr.jsh.ecommerce.product.domain.ProductRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProductRepositoryImpl implements ProductRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Product> getInStockProducts(ProductStatus productStatus) {
        String jpql = "SELECT p FROM Product p WHERE p.productStatus = :productStatus";
        return entityManager.createQuery(jpql,Product.class)
                .setParameter("productStatus", productStatus)
                .getResultList();
    }
}
