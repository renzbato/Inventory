package com.example.Inventory.controller;

import com.example.Inventory.constants.Path;
import com.example.Inventory.dto.PurchaseRequestDTO;
import com.example.Inventory.dto.PurchaseResponseDTO;
import com.example.Inventory.model.ProductModel;
import com.example.Inventory.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/product")
@RequiredArgsConstructor
@EnableSpringDataWebSupport(pageSerializationMode = EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO)
public class ProductController {

    private final ProductService service;

    // paginate product
    @GetMapping("/paginate")
    public ResponseEntity<Page<ProductModel>> paginateProduct(Pageable pageable,
                                                              @RequestParam(name = "archive", required = false) Boolean archive,
                                                              @RequestParam(name = "category_id", required = false) Integer category_id,
                                                              @RequestParam(name = "lessThanQuantity", required = false) Integer lessThanQuantity,
                                                              @RequestParam(name = "greaterThanQuantity", required = false) Integer greaterThanQuantity) {
        Page<ProductModel> response = service.paginateService(pageable, archive, category_id, lessThanQuantity, greaterThanQuantity);
        return ResponseEntity.ok(response);
    }

    // create product
    @PostMapping(Path.admin + "/create")
    public ResponseEntity<String> createProduct(@RequestBody ProductModel payload) {
        service.createService(payload);
        return ResponseEntity.ok("Product Created Successfully");
    }

    // update product
    @PatchMapping(Path.admin + "/update")
    public ResponseEntity<String> updateProduct(@RequestBody ProductModel payload) {
        service.updateService(payload);
        return ResponseEntity.ok("Product Update Successfully");
    }

    // Delete product
    @DeleteMapping(Path.admin + "/delete")
    public ResponseEntity<String> deleteProduct(@RequestParam(name = "param") int id) {
        service.deleteService(id);
        return ResponseEntity.ok("Product deleted successfully!");
    }

    // Purchase
    @PostMapping("/purchase")
    public ResponseEntity<PurchaseResponseDTO> purchase2(@RequestBody List<PurchaseRequestDTO> payload) {
        PurchaseResponseDTO productPurchased = service.validateListOfProductId(payload);
        return ResponseEntity.ok(productPurchased);
    }


    // Find by ID
    @GetMapping("/productId/{id}")
    public ResponseEntity<ProductModel> findById(@PathVariable int id) {
        ProductModel response = service.findById(id);
        return ResponseEntity.ok(response);
    }
}
