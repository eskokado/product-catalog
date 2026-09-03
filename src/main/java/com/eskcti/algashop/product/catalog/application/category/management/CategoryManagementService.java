package com.eskcti.algashop.product.catalog.application.category.management;

import com.eskcti.algashop.product.catalog.application.ApplicationMessagePublisher;
import com.eskcti.algashop.product.catalog.application.category.event.CategoryUpdatedEvent;
import com.eskcti.algashop.product.catalog.application.ResourceNotFoundException;
import com.eskcti.algashop.product.catalog.domain.model.category.Category;
import com.eskcti.algashop.product.catalog.domain.model.category.CategoryNotFoundException;
import com.eskcti.algashop.product.catalog.domain.model.category.CategoryRepository;

import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoryManagementService {

    private final CategoryRepository categoryRepository;
    private final ApplicationMessagePublisher applicationMessagePublisher;


    public UUID create(@Valid CategoryInput input) {
        Category category = new Category(input.getName(), input.getEnabled());
        categoryRepository.save(category);
        return category.getId();
    }

    public void update(UUID categoryId, CategoryInput input) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException(categoryId));
        category.setName(input.getName());
        category.setEnabled(input.getEnabled());
        categoryRepository.save(category);

        applicationMessagePublisher.send(new CategoryUpdatedEvent(
                category.getId(),
                category.getName(),
                category.getEnabled()
        ));
    }

    public void disable(UUID categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException(categoryId));
        category.setEnabled(false);
        categoryRepository.save(category);

        applicationMessagePublisher.send(new CategoryUpdatedEvent(
                category.getId(),
                category.getName(),
                category.getEnabled()
        ));
    }
}
