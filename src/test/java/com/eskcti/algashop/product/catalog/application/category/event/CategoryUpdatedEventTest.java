package com.eskcti.algashop.product.catalog.application.category.event;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class CategoryUpdatedEventTest {

    @Test
    void shouldCreateEventWithAllFields() {
        UUID categoryId = UUID.randomUUID();
        CategoryUpdatedEvent event = new CategoryUpdatedEvent(categoryId, "Notebook", true);

        assertThat(event.getCategoryId()).isEqualTo(categoryId);
        assertThat(event.getName()).isEqualTo("Notebook");
        assertThat(event.getEnabled()).isTrue();
    }

    @Test
    void shouldCreateEventWithEnabledFalse() {
        UUID categoryId = UUID.randomUUID();
        CategoryUpdatedEvent event = new CategoryUpdatedEvent(categoryId, "Mouse", false);

        assertThat(event.getCategoryId()).isEqualTo(categoryId);
        assertThat(event.getName()).isEqualTo("Mouse");
        assertThat(event.getEnabled()).isFalse();
    }
}
