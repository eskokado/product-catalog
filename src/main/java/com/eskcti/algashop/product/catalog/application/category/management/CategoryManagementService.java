package com.eskcti.algashop.product.catalog.application.category.management;

import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CategoryManagementService {
    public UUID create(@Valid CategoryInput input) {
        return null;
    }

    public void update(UUID categoryId, CategoryInput input) {
    }

    public void disable(UUID categoryId) {

    }
}
