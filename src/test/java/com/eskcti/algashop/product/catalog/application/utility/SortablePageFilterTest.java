package com.eskcti.algashop.product.catalog.application.utility;

import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Sort;

import static org.assertj.core.api.Assertions.assertThat;

class SortablePageFilterTest {

    private enum TestSortType {
        NAME("name"),
        CREATED_AT("createdAt");

        private final String propertyName;

        TestSortType(String propertyName) {
            this.propertyName = propertyName;
        }

        public String getPropertyName() {
            return propertyName;
        }
    }

    private static class ConcreteSortablePageFilter extends SortablePageFilter<TestSortType> {

        public ConcreteSortablePageFilter() {
            super();
        }

        public ConcreteSortablePageFilter(int size, int page) {
            super(size, page);
        }

        @Override
        public TestSortType getSortByPropertyOrDefault() {
            return TestSortType.NAME;
        }

        @Override
        public Sort.Direction getSortDirectionOrDefault() {
            return Sort.Direction.ASC;
        }
    }

    @Test
    void shouldHaveDefaultValues() {
        ConcreteSortablePageFilter filter = new ConcreteSortablePageFilter();

        assertThat(filter.getSize()).isEqualTo(15);
        assertThat(filter.getPage()).isEqualTo(0);
        assertThat(filter.getSortByProperty()).isNull();
        assertThat(filter.getSortDirection()).isNull();
    }

    @Test
    void shouldSetSortProperties() {
        ConcreteSortablePageFilter filter = new ConcreteSortablePageFilter();
        filter.setSortByProperty(TestSortType.CREATED_AT);
        filter.setSortDirection(Sort.Direction.DESC);

        assertThat(filter.getSortByProperty()).isEqualTo(TestSortType.CREATED_AT);
        assertThat(filter.getSortDirection()).isEqualTo(Sort.Direction.DESC);
    }

    @Test
    void shouldExtendPageFilter() {
        ConcreteSortablePageFilter filter = new ConcreteSortablePageFilter();

        assertThat(filter).isInstanceOf(PageFilter.class);
    }

    @Test
    void shouldReturnDefaultSortByProperty() {
        ConcreteSortablePageFilter filter = new ConcreteSortablePageFilter();

        assertThat(filter.getSortByPropertyOrDefault()).isEqualTo(TestSortType.NAME);
    }

    @Test
    void shouldReturnDefaultSortDirection() {
        ConcreteSortablePageFilter filter = new ConcreteSortablePageFilter();

        assertThat(filter.getSortDirectionOrDefault()).isEqualTo(Sort.Direction.ASC);
    }

    @Test
    void shouldSetSizeAndPageViaConstructor() {
        ConcreteSortablePageFilter filter = new ConcreteSortablePageFilter(20, 3);

        assertThat(filter.getSize()).isEqualTo(20);
        assertThat(filter.getPage()).isEqualTo(3);
    }

    @Test
    void shouldEqualWithSameValues() {
        ConcreteSortablePageFilter filter1 = new ConcreteSortablePageFilter(10, 0);
        filter1.setSortByProperty(TestSortType.NAME);
        filter1.setSortDirection(Sort.Direction.ASC);

        ConcreteSortablePageFilter filter2 = new ConcreteSortablePageFilter(10, 0);
        filter2.setSortByProperty(TestSortType.NAME);
        filter2.setSortDirection(Sort.Direction.ASC);

        assertThat(filter1).isEqualTo(filter2);
    }
}
