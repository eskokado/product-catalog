package com.eskcti.algashop.product.catalog.domain.model.product;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class QuantityInStockAdjustmentResultTest {

    private final UUID productId = UUID.randomUUID();

    @Test
    void shouldReturnTrueForIsOutOfStockWhenNewQuantityIsZeroAndPreviousIsNot() {
        QuantityInStockAdjustment.Result result = new QuantityInStockAdjustment.Result(productId, 10, 0);

        assertThat(result.isOutOfStock()).isTrue();
        assertThat(result.inRestocked()).isFalse();
    }

    @Test
    void shouldReturnFalseForIsOutOfStockWhenBothQuantitiesAreZero() {
        QuantityInStockAdjustment.Result result = new QuantityInStockAdjustment.Result(productId, 0, 0);

        assertThat(result.isOutOfStock()).isFalse();
        assertThat(result.inRestocked()).isFalse();
    }

    @Test
    void shouldReturnFalseForIsOutOfStockWhenNewQuantityIsNotZero() {
        QuantityInStockAdjustment.Result result = new QuantityInStockAdjustment.Result(productId, 10, 5);

        assertThat(result.isOutOfStock()).isFalse();
        assertThat(result.inRestocked()).isFalse();
    }

    @Test
    void shouldReturnTrueForInRestockedWhenNewQuantityIsPositiveAndPreviousIsZero() {
        QuantityInStockAdjustment.Result result = new QuantityInStockAdjustment.Result(productId, 0, 10);

        assertThat(result.inRestocked()).isTrue();
        assertThat(result.isOutOfStock()).isFalse();
    }

    @Test
    void shouldReturnFalseForInRestockedWhenNewQuantityIsPositiveAndPreviousIsPositive() {
        QuantityInStockAdjustment.Result result = new QuantityInStockAdjustment.Result(productId, 5, 10);

        assertThat(result.inRestocked()).isFalse();
        assertThat(result.isOutOfStock()).isFalse();
    }

    @Test
    void shouldReturnFalseForInRestockedWhenNewQuantityIsZero() {
        QuantityInStockAdjustment.Result result = new QuantityInStockAdjustment.Result(productId, 0, 0);

        assertThat(result.inRestocked()).isFalse();
    }

    @Test
    void shouldHaveCorrectFields() {
        QuantityInStockAdjustment.Result result = new QuantityInStockAdjustment.Result(productId, 5, 10);

        assertThat(result.productId()).isEqualTo(productId);
        assertThat(result.previousQuantity()).isEqualTo(5);
        assertThat(result.newQuantity()).isEqualTo(10);
    }

    @Test
    void shouldHaveEqualsAndHashCode() {
        QuantityInStockAdjustment.Result result1 = new QuantityInStockAdjustment.Result(productId, 5, 10);
        QuantityInStockAdjustment.Result result2 = new QuantityInStockAdjustment.Result(productId, 5, 10);

        assertThat(result1).isEqualTo(result2);
        assertThat(result1.hashCode()).isEqualTo(result2.hashCode());
    }

    @Test
    void shouldNotBeEqualWhenDifferentQuantities() {
        QuantityInStockAdjustment.Result result1 = new QuantityInStockAdjustment.Result(productId, 5, 10);
        QuantityInStockAdjustment.Result result2 = new QuantityInStockAdjustment.Result(productId, 5, 15);

        assertThat(result1).isNotEqualTo(result2);
    }

    @Test
    void shouldHaveToString() {
        QuantityInStockAdjustment.Result result = new QuantityInStockAdjustment.Result(productId, 5, 10);

        assertThat(result.toString()).contains(productId.toString());
    }
}
