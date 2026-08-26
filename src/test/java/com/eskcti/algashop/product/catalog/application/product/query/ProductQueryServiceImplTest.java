package com.eskcti.algashop.product.catalog.application.product.query;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

import com.eskcti.algashop.product.catalog.infrastructure.persistence.product.ProductQueryServiceImpl;

class ProductQueryServiceImplTest {

    private final ProductQueryServiceImpl service = new ProductQueryServiceImpl();

    @Test
    void shouldFindByIdReturningNullUntilPersistenceIsImplemented() {
        assertThat(service.findById(UUID.randomUUID())).isNull();
    }

    @Test
    void shouldFilterReturningNullUntilPersistenceIsImplemented() {
        assertThat(service.filter(10, 0)).isNull();
    }
}
