package com.eskcti.algashop.product.catalog.application.category.query;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class CategoryQueryServiceImplTest {

    private final CategoryQueryServiceImpl service = new CategoryQueryServiceImpl();

    @Test
    void shouldFilterReturningNullUntilPersistenceIsImplemented() {
        assertThat(service.filter(10, 0)).isNull();
    }

    @Test
    void shouldFindByIdReturningNullUntilPersistenceIsImplemented() {
        assertThat(service.findById(UUID.randomUUID())).isNull();
    }
}
