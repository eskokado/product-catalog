package com.eskcti.algashop.product.catalog.application.category.query;

import com.eskcti.algashop.product.catalog.domain.model.DomainEntityNotFoundException;
import com.eskcti.algashop.product.catalog.application.utility.Mapper;
import com.eskcti.algashop.product.catalog.domain.model.category.Category;
import com.eskcti.algashop.product.catalog.domain.model.category.CategoryRepository;
import com.eskcti.algashop.product.catalog.infrastructure.persistence.category.CategoryQueryServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

class CategoryQueryServiceImplTest {

    private final CategoryRepository categoryRepository = Mockito.mock(CategoryRepository.class);
    private final Mapper mapper = Mockito.mock(Mapper.class);
    private final MongoOperations mongoOperations = Mockito.mock(MongoOperations.class);
    private final CategoryQueryServiceImpl service = new CategoryQueryServiceImpl(categoryRepository, mapper, mongoOperations);

    @Test
    void shouldFilterReturningEmptyWhenNoCategoriesFound() {
        CategoryFilter filter = new CategoryFilter();
        filter.setPage(0);
        filter.setSize(10);

        Mockito.when(mongoOperations.count(any(Query.class), eq(Category.class))).thenReturn(0L);

        var result = service.filter(filter);

        assertThat(result.getContent()).isEmpty();
        assertThat(result.getNumber()).isEqualTo(0);
        assertThat(result.getSize()).isEqualTo(10);
        assertThat(result.getTotalElements()).isEqualTo(0);
        assertThat(result.getTotalPages()).isEqualTo(0);
    }

    @Test
    void shouldFilterWithEnabledTrue() {
        CategoryFilter filter = new CategoryFilter();
        filter.setPage(0);
        filter.setSize(10);
        filter.setEnabled(true);

        Mockito.when(mongoOperations.count(any(Query.class), eq(Category.class))).thenReturn(0L);

        var result = service.filter(filter);

        assertThat(result.getContent()).isEmpty();
    }

    @Test
    void shouldFilterWithEnabledFalse() {
        CategoryFilter filter = new CategoryFilter();
        filter.setPage(0);
        filter.setSize(10);
        filter.setEnabled(false);

        Mockito.when(mongoOperations.count(any(Query.class), eq(Category.class))).thenReturn(0L);

        var result = service.filter(filter);

        assertThat(result.getContent()).isEmpty();
    }

    @Test
    void shouldFilterWithNameNotBlank() {
        CategoryFilter filter = new CategoryFilter();
        filter.setPage(0);
        filter.setSize(10);
        filter.setName("Electronics");

        Mockito.when(mongoOperations.count(any(Query.class), eq(Category.class))).thenReturn(0L);

        var result = service.filter(filter);

        assertThat(result.getContent()).isEmpty();
    }

    @Test
    void shouldFilterReturningCategoriesWhenFound() {
        CategoryFilter filter = new CategoryFilter();
        filter.setPage(0);
        filter.setSize(10);

        Category category = new Category("Electronics", true);
        CategoryDetailOutput output = CategoryDetailOutput.builder()
                .name("Electronics")
                .enabled(true)
                .build();

        Mockito.when(mongoOperations.count(any(Query.class), eq(Category.class))).thenReturn(1L);
        Mockito.when(mongoOperations.find(any(Query.class), eq(Category.class))).thenReturn(List.of(category));
        Mockito.when(mapper.convert(category, CategoryDetailOutput.class)).thenReturn(output);

        var result = service.filter(filter);

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getTotalPages()).isEqualTo(1);
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
                .isInstanceOf(DomainEntityNotFoundException.class);
    }
}
