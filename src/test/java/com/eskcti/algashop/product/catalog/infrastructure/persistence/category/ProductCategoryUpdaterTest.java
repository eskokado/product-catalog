package com.eskcti.algashop.product.catalog.infrastructure.persistence.category;

import com.eskcti.algashop.product.catalog.application.category.event.CategoryUpdatedEvent;
import com.eskcti.algashop.product.catalog.domain.model.product.Product;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

class ProductCategoryUpdaterTest {

    private final MongoOperations mongoOperations = Mockito.mock(MongoOperations.class);
    private final ProductCategoryUpdater updater = new ProductCategoryUpdater(mongoOperations);

    @Test
    void shouldCopyCategoryDataToProducts() {
        UUID categoryId = UUID.randomUUID();
        CategoryUpdatedEvent event = new CategoryUpdatedEvent(categoryId, "Notebook Gamer", true);

        assertThatCode(() -> updater.copyCategoryDataToProducts(event)).doesNotThrowAnyException();

        ArgumentCaptor<Query> queryCaptor = ArgumentCaptor.forClass(Query.class);
        ArgumentCaptor<Update> updateCaptor = ArgumentCaptor.forClass(Update.class);
        ArgumentCaptor<Class<Product>> classCaptor = ArgumentCaptor.forClass(Class.class);

        Mockito.verify(mongoOperations).updateMulti(queryCaptor.capture(), updateCaptor.capture(), classCaptor.capture());

        Query capturedQuery = queryCaptor.getValue();
        assertThat(capturedQuery.getQueryObject()).containsEntry("category._id", categoryId);

        Update capturedUpdate = updateCaptor.getValue();
        Map<String, Object> setFields = (Map<String, Object>) capturedUpdate.getUpdateObject().get("$set");
        assertThat(setFields).containsEntry("category.name", "Notebook Gamer");
        assertThat(setFields).containsEntry("category.enabled", true);

        assertThat(classCaptor.getValue()).isEqualTo(Product.class);
    }
}
