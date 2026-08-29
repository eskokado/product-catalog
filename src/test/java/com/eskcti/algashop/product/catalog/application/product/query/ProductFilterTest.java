package com.eskcti.algashop.product.catalog.application.product.query;

import com.eskcti.algashop.product.catalog.application.utility.PageFilter;
import com.eskcti.algashop.product.catalog.application.utility.SortablePageFilter;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Sort;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class ProductFilterTest {

    @Test
    void shouldHaveDefaultValues() {
        ProductFilter filter = new ProductFilter();

        assertThat(filter.getSize()).isEqualTo(15);
        assertThat(filter.getPage()).isEqualTo(0);
        assertThat(filter.getTerm()).isNull();
        assertThat(filter.getHasDiscount()).isNull();
        assertThat(filter.getEnabled()).isNull();
        assertThat(filter.getInStock()).isNull();
        assertThat(filter.getPriceFrom()).isNull();
        assertThat(filter.getPriceTo()).isNull();
        assertThat(filter.getCategoriesId()).isNull();
        assertThat(filter.getAddedAtFrom()).isNull();
        assertThat(filter.getAddedAtTo()).isNull();
        assertThat(filter.getSortByProperty()).isNull();
        assertThat(filter.getSortDirection()).isNull();
    }

    @Test
    void shouldReturnDefaultSortByPropertyAsAddedAt() {
        ProductFilter filter = new ProductFilter();

        assertThat(filter.getSortByPropertyOrDefault()).isEqualTo(ProductFilter.SortType.ADDED_AT);
    }

    @Test
    void shouldReturnDefaultSortDirectionAsAsc() {
        ProductFilter filter = new ProductFilter();

        assertThat(filter.getSortDirectionOrDefault()).isEqualTo(Sort.Direction.ASC);
    }

    @Test
    void shouldSetAllFields() {
        UUID cat1 = UUID.randomUUID();
        UUID cat2 = UUID.randomUUID();
        OffsetDateTime from = OffsetDateTime.now().minusDays(10);
        OffsetDateTime to = OffsetDateTime.now();

        ProductFilter filter = new ProductFilter();
        filter.setTerm("notebook");
        filter.setHasDiscount(true);
        filter.setEnabled(false);
        filter.setInStock(true);
        filter.setPriceFrom(new BigDecimal("100"));
        filter.setPriceTo(new BigDecimal("500"));
        filter.setCategoriesId(new UUID[]{cat1, cat2});
        filter.setAddedAtFrom(from);
        filter.setAddedAtTo(to);
        filter.setSortByProperty(ProductFilter.SortType.SALE_PRICE);
        filter.setSortDirection(Sort.Direction.DESC);
        filter.setSize(20);
        filter.setPage(2);

        assertThat(filter.getTerm()).isEqualTo("notebook");
        assertThat(filter.getHasDiscount()).isTrue();
        assertThat(filter.getEnabled()).isFalse();
        assertThat(filter.getInStock()).isTrue();
        assertThat(filter.getPriceFrom()).isEqualByComparingTo(new BigDecimal("100"));
        assertThat(filter.getPriceTo()).isEqualByComparingTo(new BigDecimal("500"));
        assertThat(filter.getCategoriesId()).containsExactly(cat1, cat2);
        assertThat(filter.getAddedAtFrom()).isEqualTo(from);
        assertThat(filter.getAddedAtTo()).isEqualTo(to);
        assertThat(filter.getSortByProperty()).isEqualTo(ProductFilter.SortType.SALE_PRICE);
        assertThat(filter.getSortDirection()).isEqualTo(Sort.Direction.DESC);
        assertThat(filter.getSize()).isEqualTo(20);
        assertThat(filter.getPage()).isEqualTo(2);
    }

    @Test
    void shouldSortTypeReturnCorrectPropertyName() {
        assertThat(ProductFilter.SortType.ADDED_AT.getPropertyName()).isEqualTo("addedAt");
        assertThat(ProductFilter.SortType.SALE_PRICE.getPropertyName()).isEqualTo("salePrice");
    }

    @Test
    void shouldBeEqualWithSameValues() {
        ProductFilter filter1 = new ProductFilter();
        filter1.setTerm("test");
        filter1.setSize(10);

        ProductFilter filter2 = new ProductFilter();
        filter2.setTerm("test");
        filter2.setSize(10);

        assertThat(filter1).isEqualTo(filter2);
    }

    @Test
    void shouldExtendSortablePageFilter() {
        ProductFilter filter = new ProductFilter();

        assertThat(filter).isInstanceOf(SortablePageFilter.class);
        assertThat(filter).isInstanceOf(PageFilter.class);
    }
}
