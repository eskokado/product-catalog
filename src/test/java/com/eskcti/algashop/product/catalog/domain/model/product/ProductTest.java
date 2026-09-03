package com.eskcti.algashop.product.catalog.domain.model.product;

import com.eskcti.algashop.product.catalog.domain.model.DomainException;
import com.eskcti.algashop.product.catalog.domain.model.category.Category;
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
    private final Category category = new Category("Electronics", true);

    @Test
    void shouldCreateProductViaBuilderWithGeneratedId() {
        Product product = Product.builder()
                .name("Notebook X11")
                .brand("Deep Diver")
                .description("A Gamer Notebook")
                .enabled(true)
                .regularPrice(regularPrice)
                .salePrice(salePrice)
                .category(category)
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
        Product product = Product.builder()
                .name("Notebook X11")
                .brand("Deep Diver")
                .regularPrice(regularPrice)
                .salePrice(salePrice)
                .enabled(true)
                .category(category)
                .build();

        product.setName("Notebook X12");
        product.setBrand("Deep Diver Pro");

        assertThat(product.getName()).isEqualTo("Notebook X12");
        assertThat(product.getBrand()).isEqualTo("Deep Diver Pro");
    }

    @Test
    void shouldNotAllowBlankName() {
        Product product = Product.builder()
                .name("Notebook X11")
                .brand("Deep Diver")
                .regularPrice(regularPrice)
                .salePrice(salePrice)
                .enabled(true)
                .category(category)
                .build();

        assertThatThrownBy(() -> product.setName(null))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> product.setName(" "))
                .isInstanceOf(IllegalArgumentException.class);
        assertThat(product.getName()).isEqualTo("Notebook X11");
    }

    @Test
    void shouldNotAllowBlankBrand() {
        Product product = Product.builder()
                .name("Notebook X11")
                .brand("Deep Diver")
                .regularPrice(regularPrice)
                .salePrice(salePrice)
                .enabled(true)
                .category(category)
                .build();

        assertThatThrownBy(() -> product.setBrand(null))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> product.setBrand(""))
                .isInstanceOf(IllegalArgumentException.class);
        assertThat(product.getBrand()).isEqualTo("Deep Diver");
    }

    @Test
    void shouldSetRegularPriceWhenSalePriceIsNull() {
        Product product = Product.builder()
                .name("Notebook")
                .brand("Deep Diver")
                .regularPrice(regularPrice)
                .salePrice(salePrice)
                .enabled(true)
                .category(category)
                .build();

        product.changePrice(new BigDecimal("1600.00"), new BigDecimal("1100.00"));

        assertThat(product.getRegularPrice()).isEqualByComparingTo(new BigDecimal("1600.00"));
        assertThat(product.getSalePrice()).isEqualByComparingTo(new BigDecimal("1100.00"));
    }

    @Test
    void shouldNotAllowNullRegularPrice() {
        Product product = new Product();

        assertThatThrownBy(() -> invokeSetter(product, "setRegularPrice", new Class[]{BigDecimal.class}, (Object) null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void shouldNotAllowNegativeRegularPrice() {
        Product product = new Product();

        assertThatThrownBy(() -> invokeSetter(product, "setRegularPrice", new Class[]{BigDecimal.class}, new BigDecimal("-1.00")))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void shouldAllowRegularPriceAtOrAboveExistingSalePrice() {
        Product product = Product.builder()
                .name("Notebook")
                .brand("Deep Diver")
                .regularPrice(regularPrice)
                .salePrice(salePrice)
                .enabled(true)
                .category(category)
                .build();

        product.changePrice(new BigDecimal("1600.00"), new BigDecimal("1100.00"));

        assertThat(product.getRegularPrice()).isEqualByComparingTo(new BigDecimal("1600.00"));
    }

    @Test
    void shouldNotAllowRegularPriceBelowSalePrice() {
        Product product = Product.builder()
                .name("Notebook")
                .brand("Deep Diver")
                .regularPrice(regularPrice)
                .salePrice(salePrice)
                .enabled(true)
                .category(category)
                .build();

        assertThatThrownBy(() -> product.changePrice(new BigDecimal("500.00"), new BigDecimal("1000.00")))
                .isInstanceOf(DomainException.class);
    }

    @Test
    void shouldSetSalePriceWhenRegularPriceIsNull() {
        Product product = Product.builder()
                .name("Notebook")
                .brand("Deep Diver")
                .regularPrice(regularPrice)
                .salePrice(salePrice)
                .enabled(true)
                .category(category)
                .build();

        product.changePrice(new BigDecimal("1500.00"), new BigDecimal("900.00"));

        assertThat(product.getSalePrice()).isEqualByComparingTo(new BigDecimal("900.00"));
        assertThat(product.getRegularPrice()).isEqualByComparingTo(new BigDecimal("1500.00"));
    }

    @Test
    void shouldNotAllowNullSalePrice() {
        Product product = new Product();

        assertThatThrownBy(() -> invokeSetter(product, "setSalePrice", new Class[]{BigDecimal.class}, (Object) null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void shouldNotAllowNegativeSalePrice() {
        Product product = new Product();

        assertThatThrownBy(() -> invokeSetter(product, "setSalePrice", new Class[]{BigDecimal.class}, new BigDecimal("-1.00")))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void shouldNotAllowSalePriceAboveRegularPrice() {
        Product product = Product.builder()
                .name("Notebook")
                .brand("Deep Diver")
                .regularPrice(regularPrice)
                .salePrice(salePrice)
                .enabled(true)
                .category(category)
                .build();

        assertThatThrownBy(() -> product.changePrice(new BigDecimal("1500.00"), new BigDecimal("2000.00")))
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

    @Test
    void shouldNotHaveDiscountWhenPricesAreEqual() {
        Product product = Product.builder()
                .name("Notebook X11")
                .brand("Deep Diver")
                .regularPrice(new BigDecimal("1000.00"))
                .salePrice(new BigDecimal("1000.00"))
                .enabled(true)
                .category(category)
                .build();

        assertThat(product.getHasDiscount()).isFalse();
        assertThat(product.getDiscountPercentageRounded()).isZero();
    }

    @Test
    void shouldHaveDiscountWhenSalePriceIsLower() {
        Product product = Product.builder()
                .name("Notebook X11")
                .brand("Deep Diver")
                .regularPrice(new BigDecimal("2000.00"))
                .salePrice(new BigDecimal("1000.00"))
                .enabled(true)
                .category(category)
                .build();

        assertThat(product.getHasDiscount()).isTrue();
        assertThat(product.getDiscountPercentageRounded()).isEqualTo(50);
    }

    @Test
    void shouldNotHaveDiscountWhenNoPricesSet() {
        Product product = new Product();

        assertThat(product.getHasDiscount()).isFalse();
    }

    @Test
    void shouldSetDiscountPercentageToZeroWhenRegularPriceIsNull() {
        Product product = new Product();

        invokeSetter(product, "setSalePrice", new Class[]{BigDecimal.class}, new BigDecimal("1000.00"));

        assertThat(product.getDiscountPercentageRounded()).isZero();
    }

    @Test
    void shouldSetDiscountPercentageToZeroWhenRegularPriceIsZero() {
        Product product = new Product();

        invokeSetter(product, "setRegularPrice", new Class[]{BigDecimal.class}, BigDecimal.ZERO);

        assertThat(product.getDiscountPercentageRounded()).isZero();
    }

    @Test
    void shouldSetDiscountPercentageToZeroWhenRegularPriceIsZeroAndSalePriceExists() {
        Product product = new Product();
        forceSalePrice(product, new BigDecimal("1000.00"));
        forceRegularPrice(product, BigDecimal.ZERO);
        invokeMethod(product, "calculateDiscountPercentage");

        assertThat(product.getDiscountPercentageRounded()).isZero();
    }

    @Test
    void shouldSetDiscountPercentageToZeroWhenRegularPriceIsNullAndSalePriceExists() {
        Product product = new Product();
        forceSalePrice(product, new BigDecimal("1000.00"));
        forceRegularPrice(product, null);
        invokeMethod(product, "calculateDiscountPercentage");

        assertThat(product.getDiscountPercentageRounded()).isZero();
    }

    @Test
    void shouldSetDiscountPercentageToZeroWhenSalePriceIsNullAndRegularPriceExists() {
        Product product = new Product();
        forceRegularPrice(product, new BigDecimal("1000.00"));
        forceSalePrice(product, null);
        invokeMethod(product, "calculateDiscountPercentage");

        assertThat(product.getDiscountPercentageRounded()).isZero();
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

    private void forceRegularPrice(Product product, BigDecimal value) {
        try {
            Field field = Product.class.getDeclaredField("regularPrice");
            field.setAccessible(true);
            field.set(product, value);
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException(e);
        }
    }

    private void forceSalePrice(Product product, BigDecimal value) {
        try {
            Field field = Product.class.getDeclaredField("salePrice");
            field.setAccessible(true);
            field.set(product, value);
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException(e);
        }
    }

    private void invokeMethod(Product product, String methodName) {
        try {
            Method method = Product.class.getDeclaredMethod(methodName);
            method.setAccessible(true);
            method.invoke(product);
        } catch (java.lang.reflect.InvocationTargetException e) {
            throw (RuntimeException) e.getCause();
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

    @Test
    void shouldSetEnabledWhenWasEnabledIsNull() {
        Product product = new Product();
        product.setName("Notebook");
        product.setBrand("Deep Diver");
        product.setCategory(category);

        product.setEnabled(true);

        assertThat(product.getEnabled()).isTrue();
    }

    @Test
    void shouldSetEnabledToFalseWhenWasEnabledIsNull() {
        Product product = new Product();
        product.setName("Notebook");
        product.setBrand("Deep Diver");
        product.setCategory(category);

        product.setEnabled(false);

        assertThat(product.getEnabled()).isFalse();
    }

    @Test
    void shouldNotRegisterEventsWhenEnabledWasAlreadyTrue() {
        Product product = Product.builder()
                .name("Notebook")
                .brand("Deep Diver")
                .regularPrice(regularPrice)
                .salePrice(salePrice)
                .enabled(true)
                .category(category)
                .build();

        product.setEnabled(true);

        assertThat(product.getEnabled()).isTrue();
    }

    @Test
    void shouldNotRegisterEventsWhenEnabledWasAlreadyFalse() {
        Product product = Product.builder()
                .name("Notebook")
                .brand("Deep Diver")
                .regularPrice(regularPrice)
                .salePrice(salePrice)
                .enabled(false)
                .category(category)
                .build();

        product.setEnabled(false);

        assertThat(product.getEnabled()).isFalse();
    }

    @Test
    void shouldNotRegisterEventsWhenPricesDidNotChange() {
        Product product = Product.builder()
                .name("Notebook")
                .brand("Deep Diver")
                .regularPrice(new BigDecimal("1500.00"))
                .salePrice(new BigDecimal("1000.00"))
                .enabled(true)
                .category(category)
                .build();

        product.changePrice(new BigDecimal("1500.00"), new BigDecimal("1000.00"));

        assertThat(product.getRegularPrice()).isEqualByComparingTo(new BigDecimal("1500.00"));
        assertThat(product.getSalePrice()).isEqualByComparingTo(new BigDecimal("1000.00"));
    }

    @Test
    void shouldRegisterPlacedOnSaleEventWhenNewlyOnSale() {
        Product product = Product.builder()
                .name("Notebook")
                .brand("Deep Diver")
                .regularPrice(new BigDecimal("1500.00"))
                .salePrice(new BigDecimal("1500.00"))
                .enabled(true)
                .category(category)
                .build();

        assertThat(product.getHasDiscount()).isFalse();

        product.changePrice(new BigDecimal("1500.00"), new BigDecimal("1000.00"));

        assertThat(product.getHasDiscount()).isTrue();
        assertThat(product.getDiscountPercentageRounded()).isEqualTo(33);
    }

    @Test
    void shouldNotRegisterPlacedOnSaleEventWhenAlreadyOnSale() {
        Product product = Product.builder()
                .name("Notebook")
                .brand("Deep Diver")
                .regularPrice(new BigDecimal("1500.00"))
                .salePrice(new BigDecimal("1000.00"))
                .enabled(true)
                .category(category)
                .build();

        assertThat(product.getHasDiscount()).isTrue();

        product.changePrice(new BigDecimal("2000.00"), new BigDecimal("1200.00"));

        assertThat(product.getHasDiscount()).isTrue();
        assertThat(product.getDiscountPercentageRounded()).isEqualTo(40);
    }

    @Test
    void shouldNotRegisterPlacedOnSaleEventWhenRemovingDiscount() {
        Product product = Product.builder()
                .name("Notebook")
                .brand("Deep Diver")
                .regularPrice(new BigDecimal("1500.00"))
                .salePrice(new BigDecimal("1000.00"))
                .enabled(true)
                .category(category)
                .build();

        assertThat(product.getHasDiscount()).isTrue();

        product.changePrice(new BigDecimal("1500.00"), new BigDecimal("1500.00"));

        assertThat(product.getHasDiscount()).isFalse();
    }
}
