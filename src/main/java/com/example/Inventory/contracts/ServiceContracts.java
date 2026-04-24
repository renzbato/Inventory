package com.example.Inventory.contracts;

import com.example.Inventory.model.ProductModel;
import com.example.Inventory.model.PurchaseModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public abstract class ServiceContracts<T> {

    protected Page<T> paginateService(Pageable pageable) {
        return null;
    }

    protected void createService(T payload) {}
    protected void updateService(T payload) {}
    protected void deleteService(int id) {}
}
