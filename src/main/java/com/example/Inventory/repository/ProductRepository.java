package com.example.Inventory.repository;

import com.example.Inventory.model.ProductModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<ProductModel, Integer> {

    @Query(nativeQuery = true, value = "SELECT * FROM product WHERE :quantity")
    List<ProductModel> filterByQuantity(String quantity);
}
