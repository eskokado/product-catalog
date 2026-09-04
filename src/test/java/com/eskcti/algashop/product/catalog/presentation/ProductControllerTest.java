package com.eskcti.algashop.product.catalog.presentation;

import com.eskcti.algashop.product.catalog.application.PageModel;
import com.eskcti.algashop.product.catalog.application.product.management.ProductInput;
import com.eskcti.algashop.product.catalog.application.product.management.ProductManagementApplicationService;
import com.eskcti.algashop.product.catalog.application.product.query.ProductDetailOutput;
import com.eskcti.algashop.product.catalog.application.product.query.ProductDetailOutputTestDataBuilder;
import com.eskcti.algashop.product.catalog.application.product.query.ProductFilter;
import com.eskcti.algashop.product.catalog.application.product.query.ProductQueryService;
import com.eskcti.algashop.product.catalog.application.product.query.ProductSummaryOutput;
import com.eskcti.algashop.product.catalog.domain.model.category.CategoryNotFoundException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ProductControllerTest {

    private final ProductQueryService productQueryService = Mockito.mock(ProductQueryService.class);
    private final ProductManagementApplicationService productManagementApplicationService =
            Mockito.mock(ProductManagementApplicationService.class);
    private final ProductController controller =
            new ProductController(productQueryService, productManagementApplicationService);

    @Test
    void shouldCreateProductAndReturnDetail() {
        UUID productId = UUID.randomUUID();
        ProductInput input = ProductInput.builder().name("Notebook X11").build();
        ProductDetailOutput output = ProductDetailOutputTestDataBuilder.aProduct()
                .id(productId)
                .build();
        Mockito.when(productManagementApplicationService.create(input)).thenReturn(productId);
        Mockito.when(productQueryService.findById(productId)).thenReturn(output);

        ProductDetailOutput result = controller.create(input);

        assertThat(result).isSameAs(output);
    }

    @Test
    void shouldWrapCategoryNotFoundAsUnprocessableContentWhenCreatingProduct() {
        ProductInput input = ProductInput.builder().name("Notebook X11").build();
        Mockito.when(productManagementApplicationService.create(input))
                .thenThrow(new CategoryNotFoundException(UUID.randomUUID()));

        assertThatThrownBy(() -> controller.create(input))
                .isInstanceOf(UnprocessableContentException.class);
    }

    @Test
    void shouldFindByIdDelegatingToQueryService() {
        UUID productId = UUID.randomUUID();
        ProductDetailOutput output = ProductDetailOutputTestDataBuilder.aProduct()
                .id(productId)
                .build();
        Mockito.when(productQueryService.findById(productId)).thenReturn(output);

        assertThat(controller.findById(productId)).isSameAs(output);
    }

    @Test
    void shouldUpdateDelegatingToManagementService() {
        UUID productId = UUID.randomUUID();
        ProductInput input = ProductInput.builder().name("Notebook X11").build();
        ProductDetailOutput output = ProductDetailOutputTestDataBuilder.aProduct()
                .id(productId)
                .build();
        Mockito.when(productQueryService.findById(productId)).thenReturn(output);

        ProductDetailOutput result = controller.update(productId, input);

        Mockito.verify(productManagementApplicationService).update(productId, input);
        assertThat(result).isSameAs(output);
    }

    @Test
    void shouldDeleteDisablingProduct() {
        UUID productId = UUID.randomUUID();

        controller.disable(productId);

        Mockito.verify(productManagementApplicationService).disable(productId);
    }

    @Test
    void shouldEnableProduct() {
        UUID productId = UUID.randomUUID();

        controller.enable(productId);

        Mockito.verify(productManagementApplicationService).enable(productId);
    }

    @Test
    void shouldFilterDelegatingToQueryService() {
        ProductFilter productFilter = new ProductFilter();
        productFilter.setSize(10);
        productFilter.setPage(0);

        PageModel<ProductSummaryOutput> page = PageModel.<ProductSummaryOutput>builder()
                .number(0)
                .size(10)
                .totalPages(1)
                .totalElements(1)
                .content(List.of(ProductSummaryOutput.builder().name("Notebook").build()))
                .build();
        Mockito.when(productQueryService.filter(productFilter)).thenReturn(page);

        assertThat(controller.filter(productFilter)).isSameAs(page);
    }

    @Test
    void shouldRestockProduct() {
        UUID productId = UUID.randomUUID();
        ProductQuantityModel model = new ProductQuantityModel();
        model.setQuantity(10);

        controller.restock(productId, model);

        Mockito.verify(productManagementApplicationService).restock(productId, 10);
    }

    @Test
    void shouldWithdrawProduct() {
        UUID productId = UUID.randomUUID();
        ProductQuantityModel model = new ProductQuantityModel();
        model.setQuantity(5);

        controller.withdraw(productId, model);

        Mockito.verify(productManagementApplicationService).withdraw(productId, 5);
    }
}
