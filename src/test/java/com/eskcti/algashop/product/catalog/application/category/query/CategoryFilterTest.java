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
}
