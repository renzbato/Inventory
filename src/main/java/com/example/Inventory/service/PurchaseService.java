package com.example.Inventory.service;

import com.example.Inventory.contracts.ServiceContracts;
import com.example.Inventory.model.PurchaseModel;
import com.example.Inventory.repository.PurchaseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PurchaseService extends ServiceContracts<PurchaseModel> {
    private final PurchaseRepository repo;

    // Paginate
    @Override
    public Page<PurchaseModel> paginateService(Pageable pageable) {
        return repo.findAll(pageable);
    }

    // Create
    @Override
    public void createService(PurchaseModel payload) {
        repo.save(payload);
    }

    // Update
    @Override
    public void updateService(PurchaseModel payload) {
        Optional<PurchaseModel> existingProduct = repo.findById(payload.getId());
        if(existingProduct.isEmpty()) {
            throw new RuntimeException("Product does not exists!");
        }
        PurchaseModel newProduct = existingProduct.get();
        newProduct.setProductId(payload.getProductId());
        newProduct.setPrice(payload.getPrice());
        newProduct.setQuantity(payload.getQuantity());
        repo.save(newProduct);
    }

    // Delete
    @Override
    public void deleteService(int id) {
        Optional<PurchaseModel> existingProduct = repo.findById(id);

        if(existingProduct.isEmpty()) {
            throw new RuntimeException("Product does not exists!");
        }

        repo.deleteById(existingProduct.get().getId());
    }

}
