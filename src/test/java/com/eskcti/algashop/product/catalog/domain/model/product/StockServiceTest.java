package com.eskcti.algashop.product.catalog.domain.model.product;

import com.eskcti.algashop.product.catalog.domain.model.DomainEventPublisher;
import com.eskcti.algashop.product.catalog.domain.model.DomainException;
import com.eskcti.algashop.product.catalog.domain.model.category.Category;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class StockServiceTest {

    private final QuantityInStockAdjustment quantityInStockAdjustment = Mockito.mock(QuantityInStockAdjustment.class);
    private final DomainEventPublisher domainEventPublisher = Mockito.mock(DomainEventPublisher.class);
    private final StockService stockService = new StockService(quantityInStockAdjustment, domainEventPublisher);

    private Product createProduct() {
        Category category = new Category("Electronics", true);
        return Product.builder()
                .name("Smartphone")
                .brand("TechBrand")
                .description("A smartphone")
                .regularPrice(BigDecimal.valueOf(999.99))
                .salePrice(BigDecimal.valueOf(899.99))
                .enabled(true)
                .category(category)
                .build();
    }

    @Test
    void shouldRestockProductAndPublishEventWhenWasOutOfStock() {
        Product product = createProduct();
        UUID productId = product.getId();

        QuantityInStockAdjustment.Result result = new QuantityInStockAdjustment.Result(productId, 0, 10);
        when(quantityInStockAdjustment.increase(productId, 10)).thenReturn(result);

        stockService.restock(product, 10);

        verify(quantityInStockAdjustment).increase(productId, 10);
        ArgumentCaptor<Object> eventCaptor = ArgumentCaptor.forClass(Object.class);
        verify(domainEventPublisher).publish(eventCaptor.capture());
        assertThat(eventCaptor.getValue()).isInstanceOf(ProductRestockedEvent.class);
        assertThat(((ProductRestockedEvent) eventCaptor.getValue()).getProductId()).isEqualTo(productId);
    }

    @Test
    void shouldRestockProductWithoutPublishingEventWhenWasNotOutOfStock() {
        Product product = createProduct();
        UUID productId = product.getId();

        QuantityInStockAdjustment.Result result = new QuantityInStockAdjustment.Result(productId, 5, 15);
        when(quantityInStockAdjustment.increase(productId, 10)).thenReturn(result);

        stockService.restock(product, 10);

        verify(quantityInStockAdjustment).increase(productId, 10);
        verify(domainEventPublisher, never()).publish(any());
    }

    @Test
    void shouldThrowWhenRestockingWithInvalidQuantity() {
        Product product = createProduct();

        assertThatThrownBy(() -> stockService.restock(product, 0))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> stockService.restock(product, -1))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void shouldThrowWhenRestockingFails() {
        Product product = createProduct();
        UUID productId = product.getId();

        when(quantityInStockAdjustment.increase(productId, 10)).thenThrow(new RuntimeException("DB error"));

        assertThatThrownBy(() -> stockService.restock(product, 10))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining(productId.toString());
    }

    @Test
    void shouldThrowWhenRestockingNullProduct() {
        assertThatThrownBy(() -> stockService.restock(null, 10))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void shouldWithdrawProductAndPublishEventWhenBecomesOutOfStock() {
        Product product = createProduct();
        UUID productId = product.getId();

        QuantityInStockAdjustment.Result result = new QuantityInStockAdjustment.Result(productId, 10, 0);
        when(quantityInStockAdjustment.decrease(productId, 10)).thenReturn(result);

        stockService.withdraw(product, 10);

        verify(quantityInStockAdjustment).decrease(productId, 10);
        ArgumentCaptor<Object> eventCaptor = ArgumentCaptor.forClass(Object.class);
        verify(domainEventPublisher).publish(eventCaptor.capture());
        assertThat(eventCaptor.getValue()).isInstanceOf(ProductSoldOutEvent.class);
        assertThat(((ProductSoldOutEvent) eventCaptor.getValue()).getProductId()).isEqualTo(productId);
    }

    @Test
    void shouldWithdrawProductWithoutPublishingEventWhenNotOutOfStock() {
        Product product = createProduct();
        UUID productId = product.getId();

        QuantityInStockAdjustment.Result result = new QuantityInStockAdjustment.Result(productId, 10, 5);
        when(quantityInStockAdjustment.decrease(productId, 5)).thenReturn(result);

        stockService.withdraw(product, 5);

        verify(quantityInStockAdjustment).decrease(productId, 5);
        verify(domainEventPublisher, never()).publish(any());
    }

    @Test
    void shouldThrowWhenWithdrawingWithInvalidQuantity() {
        Product product = createProduct();

        assertThatThrownBy(() -> stockService.withdraw(product, 0))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> stockService.withdraw(product, -1))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void shouldThrowWhenWithdrawFails() {
        Product product = createProduct();
        UUID productId = product.getId();

        when(quantityInStockAdjustment.decrease(productId, 5)).thenThrow(new RuntimeException("DB error"));

        assertThatThrownBy(() -> stockService.withdraw(product, 5))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining(productId.toString());
    }

    @Test
    void shouldThrowWhenWithdrawingNullProduct() {
        assertThatThrownBy(() -> stockService.withdraw(null, 5))
                .isInstanceOf(NullPointerException.class);
    }
}
