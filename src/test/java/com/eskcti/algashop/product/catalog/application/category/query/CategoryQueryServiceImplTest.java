package com.eskcti.algashop.product.catalog.application.category.query;

import com.eskcti.algashop.product.catalog.application.ResourceNotFoundException;
import com.eskcti.algashop.product.catalog.application.utility.Mapper;
import com.eskcti.algashop.product.catalog.domain.model.category.Category;
import com.eskcti.algashop.product.catalog.domain.model.category.CategoryRepository;
import com.eskcti.algashop.product.catalog.infrastructure.persistence.category.CategoryQueryServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

class CategoryQueryServiceImplTest {

    private final CategoryRepository categoryRepository = Mockito.mock(CategoryRepository.class);
    private final Mapper mapper = Mockito.mock(Mapper.class);
    private final CategoryQueryServiceImpl service = new CategoryQueryServiceImpl(categoryRepository, mapper);

    @Test
    void shouldFilterReturningNullUntilPersistenceIsImplemented() {
        assertThat(service.filter(10, 0)).isNull();
    }

    @Test
    void shouldFindByIdReturningMappedOutput() {
        UUID categoryId = UUID.randomUUID();
        Category category = new Category("Notebook", true);
        CategoryDetailOutput output = CategoryDetailOutput.builder()
                .id(categoryId)
                .name("Notebook")
                .enabled(true)
                .build();

        Mockito.when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        Mockito.when(mapper.convert(category, CategoryDetailOutput.class)).thenReturn(output);

        assertThat(service.findById(categoryId)).isEqualTo(output);
    }

    @Test
    void shouldThrowWhenCategoryDoesNotExist() {
        UUID categoryId = UUID.randomUUID();
        Mockito.when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        assertThatCode(() -> service.findById(categoryId))
                .isInstanceOf(ResourceNotFoundException.class);
    }
}
