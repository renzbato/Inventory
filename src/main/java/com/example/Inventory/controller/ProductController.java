package com.example.Inventory.controller;

import com.example.Inventory.constants.Path;
import com.example.Inventory.dto.PurchaseRequestDTO;
import com.example.Inventory.dto.PurchaseResponseDTO;
import com.example.Inventory.model.ProductModel;
import com.example.Inventory.service.AccountService;
import com.example.Inventory.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/product")
@RequiredArgsConstructor
@EnableSpringDataWebSupport(pageSerializationMode = EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO)
public class ProductController {

    private final ProductService service;
    private final AccountService accountService;

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
        accountService.validSupplier(payload.getSupplier());
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
    @PostMapping(Path.publicPath + "/purchase")
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

    // Low on stock
    @GetMapping(Path.admin + "/lowOnStock")
    public ResponseEntity<Map<String, Object>> lowOnStock(@RequestParam(name = "stock", defaultValue = "0") int stock) {
        Map<String, Object> response = service.lowOnStock(stock);
        return ResponseEntity.ok(response);
    }

    // Inventory summary
    @GetMapping(Path.admin + "/productSummary")
    public ResponseEntity<Map<String, Object>> productSummary() {
        Map<String, Object> response = service.productSummary();
        return ResponseEntity.ok(response);
    }

    // Sales report
    @GetMapping(Path.admin + "/salesReport")
    public ResponseEntity<Map<String, Object>> salesReport(@RequestParam(name = "from") LocalDate from,
                                                           @RequestParam(name = "to") LocalDate to) {
        Map<String, Object> response = service.salesReport(from, to);
        return ResponseEntity.ok(response);
    }
}
