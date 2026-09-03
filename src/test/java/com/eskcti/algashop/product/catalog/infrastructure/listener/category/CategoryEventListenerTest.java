package com.eskcti.algashop.product.catalog.infrastructure.listener.category;

import com.eskcti.algashop.product.catalog.application.category.event.CategoryUpdatedEvent;
import com.eskcti.algashop.product.catalog.infrastructure.persistence.category.ProductCategoryUpdater;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatCode;

class CategoryEventListenerTest {

    private final ProductCategoryUpdater productCategoryUpdater = Mockito.mock(ProductCategoryUpdater.class);
    private final CategoryEventListener listener = new CategoryEventListener(productCategoryUpdater);

    @Test
    void shouldHandleCategoryUpdatedEvent() {
        CategoryUpdatedEvent event = new CategoryUpdatedEvent(
                UUID.randomUUID(), "Notebook", true);

        assertThatCode(() -> listener.handle(event)).doesNotThrowAnyException();
        Mockito.verify(productCategoryUpdater).copyCategoryDataToProducts(event);
    }
}
