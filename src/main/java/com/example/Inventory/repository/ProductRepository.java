package com.example.Inventory.repository;

import com.example.Inventory.model.ProductModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<ProductModel, Integer> {

    // fetch all with filter
    @Query(nativeQuery = true, value = "SELECT * FROM product " +
            "WHERE archive = :archive OR " +
            "category_id = :category_id OR " +
            "quantity <= :lessThanQuantity OR " +
            "quantity >= :greaterThanQuantity")
    Page<ProductModel> fetchAll(Pageable pageable,
                                Boolean archive,
                                Integer category_id,
                                Integer lessThanQuantity,
                                Integer greaterThanQuantity);

    // find by name
    @Query(nativeQuery = true, value = "SELECT * FROM product WHERE lower(name) = lower(:name)")
    Optional<ProductModel> findByName(String name);

    // low on stock
    @Query(nativeQuery = true, value = "SELECT * FROM product WHERE quantity <= :stock AND archive = false")
    List<ProductModel> lowStock(int stock);

    // total products
    @Query(nativeQuery = true, value = "SELECT * FROM product WHERE archive = false AND quantity > 0")
    List<ProductModel> totalProducts();

    // total out of stock
    @Query(nativeQuery = true, value = "SELECT COUNT(*) FROM product WHERE quantity <= 0 AND archive = false")
    int totalOutOfStock();

    // total archived product
    @Query(nativeQuery = true, value = "SELECT COUNT(*) FROM product WHERE archive = true")
    int totalArchived();
}
