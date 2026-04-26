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

    @Query(nativeQuery = true, value = "SELECT * FROM product " +
            "WHERE archive = :archive OR " +
            "category_id = :category_id OR " +
            "quantity <= :lessThanQuantity OR " +
            "quantity >= :greaterThanQuantity")
    Page<ProductModel> fetchAllNotArchive(Pageable pageable,
                                          Boolean archive,
                                          Integer category_id,
                                          Integer lessThanQuantity,
                                          Integer greaterThanQuantity);

    @Query(nativeQuery = true, value = "SELECT * FROM product WHERE lower(name) = lower(:name)")
    Optional<ProductModel> findByName(String name);
}
