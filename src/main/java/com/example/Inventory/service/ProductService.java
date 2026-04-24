package com.example.Inventory.service;

import com.example.Inventory.dto.PurchaseRequestDTO;
import com.example.Inventory.dto.PurchaseInfoDTO;
import com.example.Inventory.dto.PurchaseResponseDTO;
import com.example.Inventory.exception.CustomRuntimeException;
import com.example.Inventory.model.ProductModel;
import com.example.Inventory.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository repo;

    // Paginate
    public Page<ProductModel> paginateService(Pageable pageable) {
        return repo.findAll(pageable);
    }

    // Create
    public void createService(ProductModel payload) {
        repo.save(payload);
    }

    // Update
    public void updateService(ProductModel payload) {
        Optional<ProductModel> existingProduct = repo.findById(payload.getId());
        if(existingProduct.isEmpty()) {
            throw new RuntimeException("Product does not exists!");
        }
        ProductModel newProduct = existingProduct.get();

        if(Objects.nonNull(payload.getName())) {
            newProduct.setName(payload.getName());
        }

        if (Objects.nonNull(payload.getPrice())) {
            newProduct.setPrice(payload.getPrice());
        }

        if(Objects.nonNull(payload.getQuantity())) {
            newProduct.setQuantity(payload.getQuantity());
        }

        repo.save(newProduct);
    }

    // Delete
    public void deleteService(int id) {
        Optional<ProductModel> existingProduct = repo.findById(id);

        if(existingProduct.isEmpty()) {
            throw new RuntimeException("Product does not exists!");
        }

        repo.deleteById(existingProduct.get().getId());
    }

    // Purchase
    public PurchaseResponseDTO purchaseService(List<PurchaseRequestDTO> payload){
        float totalPurchase = 0;
        List<PurchaseInfoDTO> purchase = new ArrayList<>();

        for(PurchaseRequestDTO id : payload) {
            Optional<ProductModel> response = repo.findById(id.productId());
            if(id.quantity() <= 0) {
                throw new CustomRuntimeException("Quantity must be greater than zero");
            }
            if(response.isEmpty()) {
                throw new CustomRuntimeException("Out of order");
            }
            
            ProductModel product = response.get();

            // throw if the purchase quantity is greater than the available product quantity
            if(product.getQuantity() - id.quantity() < 0) {
                throw new CustomRuntimeException("Can proceed with the purchase the available product for " + product.getName() + " is " + product.getQuantity());
            }

            // create purchase info
            purchase.add(PurchaseInfoDTO
                    .builder()
                            .product(product.getName())
                            .price(product.getPrice())
                            .quantity(id.quantity())
                    .build());

            // update the product quantity
            product.setQuantity(product.getQuantity() - id.quantity());
            updateService(product);

            // set total purchase amount
            totalPurchase += product.getPrice() * id.quantity();
        }
        return PurchaseResponseDTO
                .builder()
                .purchase(purchase)
                .totalPurchase(totalPurchase)
                .build();
    }

    // Find by ID
    public ProductModel findById(int id) {
        Optional<ProductModel> response = repo.findById(id);
        if(response.isEmpty()) {
            throw new CustomRuntimeException("Product does not exists");
        }
        return response.get();
    }
}
