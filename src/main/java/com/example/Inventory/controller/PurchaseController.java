package com.example.Inventory.controller;

import com.example.Inventory.model.ProductModel;
import com.example.Inventory.model.PurchaseModel;
import com.example.Inventory.service.PurchaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/purchase")
@RequiredArgsConstructor
public class PurchaseController {
    private final PurchaseService service;

    // paginate
    @GetMapping("/paginate")
    public ResponseEntity<Page<PurchaseModel>> paginate(Pageable pageable) {
        Page<PurchaseModel> response = service.paginateService(pageable);
        return ResponseEntity.ok(response);
    }

    // create
    @PostMapping("/create")
    public ResponseEntity<String> create(@RequestBody PurchaseModel payload) {
        try {
            service.createService(payload);
            return ResponseEntity.ok("Product Created Successfully");
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    // update
    @PatchMapping("/update")
    public ResponseEntity<String> update(@RequestBody PurchaseModel payload) {
        try {
            service.updateService(payload);
            return ResponseEntity.ok("Product Update Successfully");
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    // Delete
    @DeleteMapping("/delete")
    public ResponseEntity<String> delete(@RequestParam(name = "param") int id) {
        service.deleteService(id);
        return ResponseEntity.ok("Product deleted successfully!");
    }
}
