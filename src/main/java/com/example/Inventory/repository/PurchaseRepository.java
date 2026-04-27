package com.example.Inventory.repository;

import com.example.Inventory.model.PurchaseModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PurchaseRepository extends JpaRepository<PurchaseModel, Integer> {
    // filer purchase
    @Query(nativeQuery = true, value = "SELECT * FROM purchase WHERE DATE(created_at) BETWEEN :from AND :to")
    List<PurchaseModel> filterPurchase(LocalDate from, LocalDate to);
}
