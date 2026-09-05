package com.eskcti.algashop.product.catalog.domain.model.product;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.time.OffsetDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class StockMovementTest {

    private final UUID productId = UUID.randomUUID();

    @Test
    void shouldCreateStockMovementViaBuilderWithGeneratedIdAndTimestamp() {
        StockMovement movement = StockMovement.builder()
                .productId(productId)
                .movementQuantity(10)
                .previousQuantity(5)
                .newQuantity(15)
                .type(StockMovement.MovementType.STOCK_IN)
                .build();

        assertThat(movement.getId()).isNotNull();
        assertThat(movement.getOccurredAt()).isNotNull();
        assertThat(movement.getProductId()).isEqualTo(productId);
        assertThat(movement.getMovementQuantity()).isEqualTo(10);
        assertThat(movement.getPreviousQuantity()).isEqualTo(5);
        assertThat(movement.getNewQuantity()).isEqualTo(15);
        assertThat(movement.getType()).isEqualTo(StockMovement.MovementType.STOCK_IN);
    }

    @Test
    void shouldCreateStockMovementWithStockOutType() {
        StockMovement movement = StockMovement.builder()
                .productId(productId)
                .movementQuantity(3)
                .previousQuantity(10)
                .newQuantity(7)
                .type(StockMovement.MovementType.STOCK_OUT)
                .build();

        assertThat(movement.getType()).isEqualTo(StockMovement.MovementType.STOCK_OUT);
    }

    @Test
    void shouldGenerateUniqueIdsForDifferentMovements() {
        StockMovement first = StockMovement.builder()
                .productId(productId)
                .movementQuantity(5)
                .previousQuantity(0)
                .newQuantity(5)
                .type(StockMovement.MovementType.STOCK_IN)
                .build();

        StockMovement second = StockMovement.builder()
                .productId(productId)
                .movementQuantity(5)
                .previousQuantity(5)
                .newQuantity(10)
                .type(StockMovement.MovementType.STOCK_IN)
                .build();

        assertThat(first.getId()).isNotEqualTo(second.getId());
    }

    @Test
    void shouldSetOccurredAtToCurrentTime() {
        OffsetDateTime before = OffsetDateTime.now().minusSeconds(1);

        StockMovement movement = StockMovement.builder()
                .productId(productId)
                .movementQuantity(10)
                .previousQuantity(0)
                .newQuantity(10)
                .type(StockMovement.MovementType.STOCK_IN)
                .build();

        OffsetDateTime after = OffsetDateTime.now().plusSeconds(1);

        assertThat(movement.getOccurredAt()).isAfterOrEqualTo(before);
        assertThat(movement.getOccurredAt()).isBeforeOrEqualTo(after);
    }

    @Test
    void shouldBeEqualWhenIdsAreEqual() {
        StockMovement first = StockMovement.builder()
                .productId(productId)
                .movementQuantity(10)
                .previousQuantity(0)
                .newQuantity(10)
                .type(StockMovement.MovementType.STOCK_IN)
                .build();

        StockMovement second = StockMovement.builder()
                .productId(productId)
                .movementQuantity(20)
                .previousQuantity(10)
                .newQuantity(30)
                .type(StockMovement.MovementType.STOCK_OUT)
                .build();

        forceId(second, first.getId());

        assertThat(first).isEqualTo(second);
        assertThat(first.hashCode()).isEqualTo(second.hashCode());
    }

    @Test
    void shouldNotBeEqualWhenIdsAreDifferent() {
        StockMovement first = StockMovement.builder()
                .productId(productId)
                .movementQuantity(10)
                .previousQuantity(0)
                .newQuantity(10)
                .type(StockMovement.MovementType.STOCK_IN)
                .build();

        StockMovement second = StockMovement.builder()
                .productId(productId)
                .movementQuantity(10)
                .previousQuantity(0)
                .newQuantity(10)
                .type(StockMovement.MovementType.STOCK_IN)
                .build();

        assertThat(first).isNotEqualTo(second);
    }

    @Test
    void shouldNotBeEqualToNull() {
        StockMovement movement = StockMovement.builder()
                .productId(productId)
                .movementQuantity(10)
                .previousQuantity(0)
                .newQuantity(10)
                .type(StockMovement.MovementType.STOCK_IN)
                .build();

        assertThat(movement).isNotEqualTo(null);
    }

    @Test
    void shouldNotBeEqualToDifferentType() {
        StockMovement movement = StockMovement.builder()
                .productId(productId)
                .movementQuantity(10)
                .previousQuantity(0)
                .newQuantity(10)
                .type(StockMovement.MovementType.STOCK_IN)
                .build();

        assertThat(movement).isNotEqualTo("string");
    }

    @Test
    void shouldBeEqualToSameInstance() {
        StockMovement movement = StockMovement.builder()
                .productId(productId)
                .movementQuantity(10)
                .previousQuantity(0)
                .newQuantity(10)
                .type(StockMovement.MovementType.STOCK_IN)
                .build();

        assertThat(movement).isEqualTo(movement);
    }

    @Test
    void shouldHaveProtectedNoArgsConstructor() throws Exception {
        Constructor<?> constructor = StockMovement.class.getDeclaredConstructor();
        assertThat(constructor).isNotNull();
        assertThat(java.lang.reflect.Modifier.isProtected(constructor.getModifiers())).isTrue();
    }

    @Test
    void shouldHaveBothMovementTypes() {
        assertThat(StockMovement.MovementType.values()).hasSize(2);
        assertThat(StockMovement.MovementType.valueOf("STOCK_IN")).isEqualTo(StockMovement.MovementType.STOCK_IN);
        assertThat(StockMovement.MovementType.valueOf("STOCK_OUT")).isEqualTo(StockMovement.MovementType.STOCK_OUT);
    }

    @Test
    void shouldSetProductIdViaField() {
        StockMovement movement = StockMovement.builder()
                .productId(productId)
                .movementQuantity(10)
                .previousQuantity(0)
                .newQuantity(10)
                .type(StockMovement.MovementType.STOCK_IN)
                .build();

        UUID newProductId = UUID.randomUUID();
        forceField(movement, "productId", newProductId);
        assertThat(movement.getProductId()).isEqualTo(newProductId);
    }

    @Test
    void shouldSetMovementQuantityViaField() {
        StockMovement movement = StockMovement.builder()
                .productId(productId)
                .movementQuantity(10)
                .previousQuantity(0)
                .newQuantity(10)
                .type(StockMovement.MovementType.STOCK_IN)
                .build();

        forceField(movement, "movementQuantity", 25);
        assertThat(movement.getMovementQuantity()).isEqualTo(25);
    }

    @Test
    void shouldSetPreviousQuantityViaField() {
        StockMovement movement = StockMovement.builder()
                .productId(productId)
                .movementQuantity(10)
                .previousQuantity(0)
                .newQuantity(10)
                .type(StockMovement.MovementType.STOCK_IN)
                .build();

        forceField(movement, "previousQuantity", 5);
        assertThat(movement.getPreviousQuantity()).isEqualTo(5);
    }

    @Test
    void shouldSetNewQuantityViaField() {
        StockMovement movement = StockMovement.builder()
                .productId(productId)
                .movementQuantity(10)
                .previousQuantity(0)
                .newQuantity(10)
                .type(StockMovement.MovementType.STOCK_IN)
                .build();

        forceField(movement, "newQuantity", 20);
        assertThat(movement.getNewQuantity()).isEqualTo(20);
    }

    @Test
    void shouldSetTypeViaField() {
        StockMovement movement = StockMovement.builder()
                .productId(productId)
                .movementQuantity(10)
                .previousQuantity(0)
                .newQuantity(10)
                .type(StockMovement.MovementType.STOCK_IN)
                .build();

        forceField(movement, "type", StockMovement.MovementType.STOCK_OUT);
        assertThat(movement.getType()).isEqualTo(StockMovement.MovementType.STOCK_OUT);
    }

    @Test
    void shouldSetIdViaField() {
        StockMovement movement = StockMovement.builder()
                .productId(productId)
                .movementQuantity(10)
                .previousQuantity(0)
                .newQuantity(10)
                .type(StockMovement.MovementType.STOCK_IN)
                .build();

        UUID newId = UUID.randomUUID();
        forceField(movement, "id", newId);
        assertThat(movement.getId()).isEqualTo(newId);
    }

    @Test
    void shouldSetOccurredAtViaField() {
        StockMovement movement = StockMovement.builder()
                .productId(productId)
                .movementQuantity(10)
                .previousQuantity(0)
                .newQuantity(10)
                .type(StockMovement.MovementType.STOCK_IN)
                .build();

        OffsetDateTime newOccurredAt = OffsetDateTime.now().minusDays(1);
        forceField(movement, "occurredAt", newOccurredAt);
        assertThat(movement.getOccurredAt()).isEqualTo(newOccurredAt);
    }

    private void forceId(StockMovement movement, UUID id) {
        forceField(movement, "id", id);
    }

    private void forceField(StockMovement movement, String fieldName, Object value) {
        try {
            Field field = StockMovement.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(movement, value);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException(e);
        } catch (Exception e) {
            if (e.getCause() instanceof RuntimeException) {
                throw (RuntimeException) e.getCause();
            }
            throw new IllegalStateException(e);
        }
    }
}
