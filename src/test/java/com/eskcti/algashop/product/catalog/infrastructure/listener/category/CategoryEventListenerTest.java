package com.eskcti.algashop.product.catalog.infrastructure.listener.category;

import com.eskcti.algashop.product.catalog.application.category.event.CategoryUpdatedEvent;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatCode;

class CategoryEventListenerTest {

    private final CategoryEventListener listener = new CategoryEventListener();

    @Test
    void shouldHandleCategoryUpdatedEvent() {
        CategoryUpdatedEvent event = new CategoryUpdatedEvent(
                UUID.randomUUID(), "Notebook", true);

        assertThatCode(() -> listener.handle(event)).doesNotThrowAnyException();
    }
}
