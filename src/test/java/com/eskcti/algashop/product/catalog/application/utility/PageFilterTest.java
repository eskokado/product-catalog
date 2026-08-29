package com.eskcti.algashop.product.catalog.application.utility;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PageFilterTest {

    @Test
    void shouldHaveDefaultValues() {
        PageFilter filter = new PageFilter();

        assertThat(filter.getSize()).isEqualTo(15);
        assertThat(filter.getPage()).isEqualTo(0);
    }

    @Test
    void shouldSetValuesWithConstructor() {
        PageFilter filter = new PageFilter(20, 2);

        assertThat(filter.getSize()).isEqualTo(20);
        assertThat(filter.getPage()).isEqualTo(2);
    }

    @Test
    void shouldSetValuesWithSetters() {
        PageFilter filter = new PageFilter();
        filter.setSize(30);
        filter.setPage(5);

        assertThat(filter.getSize()).isEqualTo(30);
        assertThat(filter.getPage()).isEqualTo(5);
    }

    @Test
    void shouldEqualWithSameValues() {
        PageFilter filter1 = new PageFilter(10, 0);
        PageFilter filter2 = new PageFilter(10, 0);

        assertThat(filter1).isEqualTo(filter2);
    }

    @Test
    void shouldNotEqualWithDifferentValues() {
        PageFilter filter1 = new PageFilter(10, 0);
        PageFilter filter2 = new PageFilter(20, 1);

        assertThat(filter1).isNotEqualTo(filter2);
    }
}
