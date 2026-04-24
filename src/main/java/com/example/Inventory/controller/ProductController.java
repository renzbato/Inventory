package com.example.Inventory.controller;

import com.example.Inventory.dto.PurchaseRequestDTO;
import com.example.Inventory.dto.PurchaseResponseDTO;
import com.example.Inventory.model.ProductModel;
import com.example.Inventory.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService service;

    // paginate product
    @GetMapping("/paginate")
    public ResponseEntity<Page<ProductModel>> paginateProduct(Pageable pageable) {
        Page<ProductModel> response = service.paginateService(pageable);
        return ResponseEntity.ok(response);
    }

    // create product
    @PostMapping("/create")
    public ResponseEntity<String> createProduct(@RequestBody ProductModel payload) {
        try {
            service.createService(payload);
            return ResponseEntity.ok("Product Created Successfully");
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    // update product
    @PatchMapping("/update")
    public ResponseEntity<String> updateProduct(@RequestBody ProductModel payload) {
        try {
            service.updateService(payload);
            return ResponseEntity.ok("Product Update Successfully");
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    // Delete product
    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteProduct(@RequestParam(name = "param") int id) {
        service.deleteService(id);
        return ResponseEntity.ok("Product deleted successfully!");
    }

    // Purchase
    @PostMapping("/purchase")
    public ResponseEntity<PurchaseResponseDTO> purchase(@RequestBody List<PurchaseRequestDTO> payload) {
        PurchaseResponseDTO response = service.purchaseService(payload);
        return ResponseEntity.ok(response);
    }
}
