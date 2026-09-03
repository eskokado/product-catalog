package com.eskcti.algashop.product.catalog.presentation;

import com.eskcti.algashop.product.catalog.application.PageModel;
import com.eskcti.algashop.product.catalog.application.category.management.CategoryInput;
import com.eskcti.algashop.product.catalog.application.category.management.CategoryManagementService;
import com.eskcti.algashop.product.catalog.application.category.query.CategoryDetailOutput;
import com.eskcti.algashop.product.catalog.application.category.query.CategoryFilter;
import com.eskcti.algashop.product.catalog.application.category.query.CategoryOutputTestDataBuilder;
import com.eskcti.algashop.product.catalog.application.category.query.CategoryQueryService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class CategoryControllerTest {

    private final CategoryQueryService categoryQueryService = Mockito.mock(CategoryQueryService.class);
    private final CategoryManagementService categoryManagementService =
            Mockito.mock(CategoryManagementService.class);
    private final CategoryController controller =
            new CategoryController(categoryQueryService, categoryManagementService);

    @Test
    void shouldCreateCategoryAndReturnDetail() {
        UUID categoryId = UUID.randomUUID();
        CategoryInput input = CategoryInput.builder().name("Notebook").enabled(true).build();
        CategoryDetailOutput output = CategoryOutputTestDataBuilder.aCategory()
                .id(categoryId)
                .build();
        Mockito.when(categoryManagementService.create(input)).thenReturn(categoryId);
        Mockito.when(categoryQueryService.findById(categoryId)).thenReturn(output);

        CategoryDetailOutput result = controller.create(input);

        assertThat(result).isSameAs(output);
    }

    @Test
    void shouldFindByIdDelegatingToQueryService() {
        UUID categoryId = UUID.randomUUID();
        CategoryDetailOutput output = CategoryOutputTestDataBuilder.aCategory()
                .id(categoryId)
                .build();
        Mockito.when(categoryQueryService.findById(categoryId)).thenReturn(output);

        assertThat(controller.findById(categoryId)).isSameAs(output);
    }

    @Test
    void shouldUpdateDelegatingToManagementService() {
        UUID categoryId = UUID.randomUUID();
        CategoryInput input = CategoryInput.builder().name("Notebook Gamer").enabled(false).build();
        CategoryDetailOutput output = CategoryOutputTestDataBuilder.aCategory()
                .id(categoryId)
                .name("Notebook Gamer")
                .enabled(false)
                .build();
        Mockito.when(categoryQueryService.findById(categoryId)).thenReturn(output);

        CategoryDetailOutput result = controller.update(categoryId, input);

        Mockito.verify(categoryManagementService).update(categoryId, input);
        assertThat(result).isSameAs(output);
    }

    @Test
    void shouldDisableDelegatingToManagementService() {
        UUID categoryId = UUID.randomUUID();

        controller.disable(categoryId);

        Mockito.verify(categoryManagementService).disable(categoryId);
    }

    @Test
    void shouldFilterDelegatingToQueryService() {
        CategoryFilter filter = new CategoryFilter();
        filter.setSize(10);
        filter.setPage(0);

        PageModel<CategoryDetailOutput> page = PageModel.<CategoryDetailOutput>builder()
                .number(0)
                .size(10)
                .totalPages(1)
                .totalElements(1)
                .content(List.of(CategoryOutputTestDataBuilder.aCategory().build()))
                .build();
        Mockito.when(categoryQueryService.filter(filter)).thenReturn(page);

        assertThat(controller.filter(filter)).isSameAs(page);
    }
}
