package com.example.Inventory.service;

import com.example.Inventory.dto.PurchaseDTO;
import com.example.Inventory.dto.PurchaseRequestDTO;
import com.example.Inventory.dto.PurchaseResponseDTO;
import com.example.Inventory.exception.CustomRuntimeException;
import com.example.Inventory.model.ProductModel;
import com.example.Inventory.model.PurchaseModel;
import com.example.Inventory.repository.ProductRepository;
import com.example.Inventory.repository.PurchaseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository repo;
    private final PurchaseRepository purchaseRepo;

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
        // fetch product by id
        Optional<ProductModel> existingProduct = repo.findById(payload.getId());

        // throw is product does not exit
        if(existingProduct.isEmpty()) {
            throw new RuntimeException("Product does not exists!");
        }
        ProductModel newProduct = existingProduct.get();

        // set the product name if the payload is not null
        if(Objects.nonNull(payload.getName())) {
            newProduct.setName(payload.getName());
        }

        // set the product price if the payload is not null
        if (Objects.nonNull(payload.getPrice())) {
            newProduct.setPrice(payload.getPrice());
        }

        // set the product quantity if the payload is not null
        if(Objects.nonNull(payload.getQuantity())) {
            newProduct.setQuantity(payload.getQuantity());
        }

        repo.save(newProduct);
    }

    // Delete
    public void deleteService(int id) {
        // fetch product by id
        Optional<ProductModel> existingProduct = repo.findById(id);

        // throw if empty
        if(existingProduct.isEmpty()) {
            throw new CustomRuntimeException("Product does not exists!");
        }

        // delete product by id
        repo.deleteById(existingProduct.get().getId());
    }

    // Find by ID
    public ProductModel findById(int id) {

        // fetch product by id
        Optional<ProductModel> response = repo.findById(id);

        // throw if product does not exist
        if(response.isEmpty()) {
            throw new CustomRuntimeException("Product does not exists");
        }

        return response.get();
    }

    // Purchase
    @Transactional
    public PurchaseResponseDTO validateListOfProductId(List<PurchaseRequestDTO> payload) {
        float totalPurchase = 0;
        List<PurchaseDTO> purchaseList = new ArrayList<>();

        for(PurchaseRequestDTO purchase : payload) {
            Optional<ProductModel> existingProduct = repo.findById(purchase.productId());

            // throw if the asking product quantity is less than or equal to zero
            if(purchase.quantity() <= 0) {
                throw new CustomRuntimeException("Quantity must be greater than zero");
            }

            if(existingProduct.isEmpty()) {
                throw new CustomRuntimeException("Product does not exist");
            }

            ProductModel product = existingProduct.get();

            // throw if the purchase quantity is greater than the available product quantity
            if(product.getQuantity() - purchase.quantity() < 0) {
                throw new CustomRuntimeException("Can't proceed with the purchase the available product for " + product.getName() + " is " + product.getQuantity());
            }

            // update the product quantity
            product.setQuantity(product.getQuantity() - purchase.quantity());
            updateService(product);

            // save purchase
            PurchaseModel purchaseBuild = PurchaseModel
                    .builder()
                    .quantity(purchase.quantity())
                    .product(product)
                    .build();

            purchaseRepo.save(purchaseBuild);

            // set total purchase amount
            totalPurchase += product.getPrice() * purchase.quantity();

            // list purchase
            PurchaseDTO purchaseRes = PurchaseDTO
                    .builder()
                    .productId(product.getId())
                    .product(product.getName())
                    .quantity(purchase.quantity())
                    .price(product.getPrice())
                    .build();
            purchaseList.add(purchaseRes);
        }

        return PurchaseResponseDTO
                .builder()
                .purchase(purchaseList)
                .totalPurchase(totalPurchase)
                .build();
    }
}
