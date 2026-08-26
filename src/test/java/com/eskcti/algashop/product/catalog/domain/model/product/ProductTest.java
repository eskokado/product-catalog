package com.eskcti.algashop.product.catalog.domain.model.product;

import com.eskcti.algashop.product.catalog.domain.model.DomainException;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ProductTest {

    private final BigDecimal regularPrice = new BigDecimal("1500.00");
    private final BigDecimal salePrice = new BigDecimal("1000.00");

    @Test
    void shouldCreateProductViaBuilderWithGeneratedId() {
        Product product = Product.builder()
                .name("Notebook X11")
                .brand("Deep Diver")
                .description("A Gamer Notebook")
                .enabled(true)
                .regularPrice(regularPrice)
                .salePrice(salePrice)
                .build();

        assertThat(product.getId()).isNotNull();
        assertThat(product.getName()).isEqualTo("Notebook X11");
        assertThat(product.getBrand()).isEqualTo("Deep Diver");
        assertThat(product.getDescription()).isEqualTo("A Gamer Notebook");
        assertThat(product.getEnabled()).isTrue();
        assertThat(product.getRegularPrice()).isEqualByComparingTo(regularPrice);
        assertThat(product.getSalePrice()).isEqualByComparingTo(salePrice);
    }

    @Test
    void shouldUpdateDescription() {
        Product product = new Product();

        product.setDescription("A Gamer Notebook");

        assertThat(product.getDescription()).isEqualTo("A Gamer Notebook");
    }

    @Test
    void shouldUpdateNameAndBrandWithValidValues() {
        Product product = Product.builder().name("Notebook X11").brand("Deep Diver").build();

        product.setName("Notebook X12");
        product.setBrand("Deep Diver Pro");

        assertThat(product.getName()).isEqualTo("Notebook X12");
        assertThat(product.getBrand()).isEqualTo("Deep Diver Pro");
    }

    @Test
    void shouldNotAllowBlankName() {
        Product product = Product.builder().name("Notebook X11").build();

        assertThatThrownBy(() -> product.setName(null))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> product.setName(" "))
                .isInstanceOf(IllegalArgumentException.class);
        assertThat(product.getName()).isEqualTo("Notebook X11");
    }

    @Test
    void shouldNotAllowBlankBrand() {
        Product product = Product.builder().brand("Deep Diver").build();

        assertThatThrownBy(() -> product.setBrand(null))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> product.setBrand(""))
                .isInstanceOf(IllegalArgumentException.class);
        assertThat(product.getBrand()).isEqualTo("Deep Diver");
    }

    @Test
    void shouldSetRegularPriceWhenSalePriceIsNull() {
        Product product = new Product();

        product.setRegularPrice(regularPrice);

        assertThat(product.getRegularPrice()).isEqualByComparingTo(regularPrice);
        assertThat(product.getSalePrice()).isEqualByComparingTo(regularPrice);
    }

    @Test
    void shouldNotAllowNullRegularPrice() {
        Product product = new Product();

        assertThatThrownBy(() -> product.setRegularPrice(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void shouldNotAllowNegativeRegularPrice() {
        Product product = new Product();

        assertThatThrownBy(() -> product.setRegularPrice(new BigDecimal("-1.00")))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void shouldAllowRegularPriceAtOrAboveExistingSalePrice() {
        Product product = new Product();
        product.setRegularPrice(regularPrice);
        product.setSalePrice(salePrice);

        product.setRegularPrice(new BigDecimal("1600.00"));

        assertThat(product.getRegularPrice()).isEqualByComparingTo(new BigDecimal("1600.00"));
    }

    @Test
    void shouldNotAllowRegularPriceBelowSalePrice() {
        Product product = new Product();
        product.setRegularPrice(regularPrice);
        product.setSalePrice(salePrice);

        assertThatThrownBy(() -> product.setRegularPrice(new BigDecimal("500.00")))
                .isInstanceOf(DomainException.class);
    }

    @Test
    void shouldSetSalePriceWhenRegularPriceIsNull() {
        Product product = new Product();

        product.setSalePrice(salePrice);

        assertThat(product.getSalePrice()).isEqualByComparingTo(salePrice);
        assertThat(product.getRegularPrice()).isEqualByComparingTo(salePrice);
    }

    @Test
    void shouldNotAllowNullSalePrice() {
        Product product = new Product();

        assertThatThrownBy(() -> product.setSalePrice(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void shouldNotAllowNegativeSalePrice() {
        Product product = new Product();

        assertThatThrownBy(() -> product.setSalePrice(new BigDecimal("-1.00")))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void shouldNotAllowSalePriceAboveRegularPrice() {
        Product product = new Product();
        product.setSalePrice(salePrice);

        assertThatThrownBy(() -> product.setSalePrice(new BigDecimal("2000.00")))
                .isInstanceOf(DomainException.class);
    }

    @Test
    void shouldEnableAndDisableProduct() {
        Product product = new Product();
        product.setEnabled(true);

        product.disable();
        assertThat(product.getEnabled()).isFalse();

        product.enable();
        assertThat(product.getEnabled()).isTrue();
    }

    @Test
    void shouldNotAllowNullEnabled() {
        Product product = new Product();

        assertThatThrownBy(() -> product.setEnabled(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void shouldBeOutOfStockWhenQuantityIsNullOrZero() {
        Product product = new Product();

        assertThat(product.isInStock()).isFalse();

        forceQuantityInStock(product, 0);
        assertThat(product.isInStock()).isFalse();
    }

    @Test
    void shouldBeInStockWhenQuantityIsPositive() {
        Product product = new Product();
        forceQuantityInStock(product, 5);

        assertThat(product.isInStock()).isTrue();
    }

    @Test
    void shouldSetIdRejectingNullViaReflection() {
        Product product = new Product();

        assertThatThrownBy(() -> invokeSetter(product, "setId", new Class[]{UUID.class}, (Object) null))
                .isInstanceOf(NullPointerException.class);

        UUID id = UUID.randomUUID();
        invokeSetter(product, "setId", new Class[]{UUID.class}, id);
        assertThat(product.getId()).isEqualTo(id);
    }

    @Test
    void shouldSetQuantityInStockRejectingInvalidValuesViaReflection() {
        Product product = new Product();

        assertThatThrownBy(() -> invokeSetter(product, "setQuantityInStock", new Class[]{Integer.class}, (Object) null))
                .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> invokeSetter(product, "setQuantityInStock", new Class[]{Integer.class}, -1))
                .isInstanceOf(IllegalArgumentException.class);

        invokeSetter(product, "setQuantityInStock", new Class[]{Integer.class}, 10);
        assertThat(product.getQuantityInStock()).isEqualTo(10);
    }

    private void forceQuantityInStock(Product product, Integer quantity) {
        try {
            Field field = Product.class.getDeclaredField("quantityInStock");
            field.setAccessible(true);
            field.set(product, quantity);
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException(e);
        }
    }

    private void invokeSetter(Product product, String methodName, Class<?>[] parameterTypes, Object argument) {
        try {
            Method method = Product.class.getDeclaredMethod(methodName, parameterTypes);
            method.setAccessible(true);
            method.invoke(product, argument);
        } catch (java.lang.reflect.InvocationTargetException e) {
            throw (RuntimeException) e.getCause();
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException(e);
        }
    }
}
