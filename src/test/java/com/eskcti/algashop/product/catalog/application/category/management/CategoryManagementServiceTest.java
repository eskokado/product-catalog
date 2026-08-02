package com.eskcti.algashop.product.catalog.application.category.management;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

class CategoryManagementServiceTest {

    private final CategoryManagementService service = new CategoryManagementService();

    @Test
    void shouldCreateCategoryReturningNullUntilPersistenceIsImplemented() {
        CategoryInput input = CategoryInput.builder()
                .name("Notebook")
                .enabled(true)
                .build();

        assertThat(service.create(input)).isNull();
    }

    @Test
    void shouldAcceptUpdateWithoutSideEffectsUntilPersistenceIsImplemented() {
        CategoryInput input = CategoryInput.builder()
                .name("Notebook")
                .enabled(true)
                .build();

        assertThatCode(() -> service.update(UUID.randomUUID(), input)).doesNotThrowAnyException();
    }

    @Test
    void shouldAcceptDisableWithoutSideEffectsUntilPersistenceIsImplemented() {
        assertThatCode(() -> service.disable(UUID.randomUUID())).doesNotThrowAnyException();
    }
}
