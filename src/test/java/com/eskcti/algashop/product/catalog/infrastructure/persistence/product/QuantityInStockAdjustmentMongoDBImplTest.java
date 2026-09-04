package com.eskcti.algashop.product.catalog.infrastructure.persistence.product;

import com.eskcti.algashop.product.catalog.domain.model.product.Product;
import com.eskcti.algashop.product.catalog.domain.model.product.ProductNotFoundException;
import com.eskcti.algashop.product.catalog.domain.model.product.QuantityInStockAdjustment;
import org.bson.Document;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.lang.reflect.Field;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

class QuantityInStockAdjustmentMongoDBImplTest {

    private final MongoOperations mongoOperations = Mockito.mock(MongoOperations.class);
    private final QuantityInStockAdjustmentMongoDBImpl adjustment =
            new QuantityInStockAdjustmentMongoDBImpl(mongoOperations);

    private void setQuantityInStock(Product product, int quantity) throws Exception {
        Field field = Product.class.getDeclaredField("quantityInStock");
        field.setAccessible(true);
        field.set(product, quantity);
    }

    private Product createProductWithStock(int stock) throws Exception {
        Product product = Product.builder()
                .name("Test Product")
                .brand("Brand")
                .description("Description")
                .enabled(true)
                .regularPrice(java.math.BigDecimal.valueOf(100))
                .salePrice(java.math.BigDecimal.valueOf(80))
                .category(new com.eskcti.algashop.product.catalog.domain.model.category.Category("Cat", true))
                .build();
        setQuantityInStock(product, stock);
        return product;
    }

    private AggregationResults<Document> mockAggregation(int quantityInStock) {
        Document doc = new Document("quantityInStock", quantityInStock);
        AggregationResults<Document> aggregationResults = Mockito.mock(AggregationResults.class);
        when(aggregationResults.getUniqueMappedResult()).thenReturn(doc);
        when(mongoOperations.aggregate(any(Aggregation.class), eq(Product.class), eq(Document.class)))
                .thenReturn(aggregationResults);
        return aggregationResults;
    }

    @Test
    void shouldIncreaseStock() throws Exception {
        UUID productId = UUID.randomUUID();
        mockAggregation(10);

        Product productUpdated = createProductWithStock(15);
        when(mongoOperations.findAndModify(any(Query.class), any(Update.class), any(), eq(Product.class)))
                .thenReturn(productUpdated);

        QuantityInStockAdjustment.Result result = adjustment.increase(productId, 5);

        assertThat(result.productId()).isEqualTo(productId);
        assertThat(result.previousQuantity()).isEqualTo(10);
        assertThat(result.newQuantity()).isEqualTo(15);
    }

    @Test
    void shouldDecreaseStock() throws Exception {
        UUID productId = UUID.randomUUID();
        mockAggregation(20);

        Product productUpdated = createProductWithStock(15);
        when(mongoOperations.findAndModify(any(Query.class), any(Update.class), any(), eq(Product.class)))
                .thenReturn(productUpdated);

        QuantityInStockAdjustment.Result result = adjustment.decrease(productId, 5);

        assertThat(result.productId()).isEqualTo(productId);
        assertThat(result.previousQuantity()).isEqualTo(20);
        assertThat(result.newQuantity()).isEqualTo(15);
    }

    @Test
    void shouldThrowProductNotFoundExceptionWhenProductDoesNotExist() {
        UUID productId = UUID.randomUUID();
        AggregationResults<Document> aggregationResults = Mockito.mock(AggregationResults.class);
        when(aggregationResults.getUniqueMappedResult()).thenReturn(null);
        when(mongoOperations.aggregate(any(Aggregation.class), eq(Product.class), eq(Document.class)))
                .thenReturn(aggregationResults);

        assertThatThrownBy(() -> adjustment.increase(productId, 5))
                .isInstanceOf(ProductNotFoundException.class);
    }

    @Test
    void shouldThrowStockUpdateFailedWhenFindAndModifyReturnsNull() {
        UUID productId = UUID.randomUUID();
        mockAggregation(10);

        when(mongoOperations.findAndModify(any(Query.class), any(Update.class), any(), eq(Product.class)))
                .thenReturn(null);

        assertThatThrownBy(() -> adjustment.increase(productId, 5))
                .isInstanceOf(StockUpdateFailed.class)
                .hasMessageContaining(productId.toString());
    }
}
