package ru.numbDev.wildberries.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.numbDev.wildberries.db.entity.ProductEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, String> {

    @Query(nativeQuery = true, value = "select * from product where product_id = :id")
    Optional<ProductEntity> findByProductId(@Param("id") Long id);

    @Query(nativeQuery = true, value = "select * from product limit 20 offset 20 * :page")
    List<ProductEntity> getProductsWithPagging(@Param("page") Integer page);
}
