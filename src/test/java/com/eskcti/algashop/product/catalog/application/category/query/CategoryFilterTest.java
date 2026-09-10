package com.eskcti.algashop.product.catalog.application.category.query;

import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Sort;

import static org.assertj.core.api.Assertions.assertThat;

class CategoryFilterTest {

    @Test
    void shouldReturnDefaultSortByPropertyWhenNull() {
        CategoryFilter filter = new CategoryFilter();
        assertThat(filter.getSortByPropertyOrDefault()).isEqualTo(CategoryFilter.SortType.NAME);
    }

    @Test
    void shouldReturnConfiguredSortByPropertyWhenNotNull() {
        CategoryFilter filter = new CategoryFilter();
        filter.setSortByProperty(CategoryFilter.SortType.NAME);
        assertThat(filter.getSortByPropertyOrDefault()).isEqualTo(CategoryFilter.SortType.NAME);
    }

    @Test
    void shouldReturnDefaultSortDirectionWhenNull() {
        CategoryFilter filter = new CategoryFilter();
        assertThat(filter.getSortDirectionOrDefault()).isEqualTo(Sort.Direction.ASC);
    }

    @Test
    void shouldReturnConfiguredSortDirectionWhenNotNull() {
        CategoryFilter filter = new CategoryFilter();
        filter.setSortDirection(Sort.Direction.DESC);
        assertThat(filter.getSortDirectionOrDefault()).isEqualTo(Sort.Direction.DESC);
    }

    @Test
    void shouldReturnCacheableWhenDefaultFilter() {
        CategoryFilter filter = CategoryFilter.defaultFilter();
        assertThat(filter.isCacheable()).isTrue();
    }

    @Test
    void shouldNotBeCacheableWhenFilterDiffersFromDefault() {
        CategoryFilter filter = CategoryFilter.defaultFilter();
        filter.setName("Electronics");
        assertThat(filter.isCacheable()).isFalse();
    }

    @Test
    void shouldCreateDefaultFilterWithExpectedValues() {
        CategoryFilter filter = CategoryFilter.defaultFilter();

        assertThat(filter.getName()).isNull();
        assertThat(filter.getEnabled()).isTrue();
        assertThat(filter.getPage()).isEqualTo(0);
        assertThat(filter.getSize()).isEqualTo(15);
        assertThat(filter.getSortDirection()).isEqualTo(Sort.Direction.ASC);
        assertThat(filter.getSortByProperty()).isEqualTo(CategoryFilter.SortType.NAME);
    }

    @Test
    void shouldReturnPropertyNameFromSortType() {
        assertThat(CategoryFilter.SortType.NAME.getPropertyName()).isEqualTo("name");
    }
}
