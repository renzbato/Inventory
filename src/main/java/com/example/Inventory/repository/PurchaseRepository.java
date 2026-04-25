package com.example.Inventory.repository;

import com.example.Inventory.model.PurchaseModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PurchaseRepository extends JpaRepository<PurchaseModel, Integer> {
}
