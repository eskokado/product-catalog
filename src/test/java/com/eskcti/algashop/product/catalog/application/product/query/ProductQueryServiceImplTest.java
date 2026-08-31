package com.eskcti.algashop.product.catalog.application.product.query;

import com.eskcti.algashop.product.catalog.application.utility.Mapper;
import com.eskcti.algashop.product.catalog.domain.model.category.Category;
import com.eskcti.algashop.product.catalog.domain.model.product.Product;
import com.eskcti.algashop.product.catalog.domain.model.product.ProductNotFoundException;
import com.eskcti.algashop.product.catalog.domain.model.product.ProductRepository;
import com.eskcti.algashop.product.catalog.infrastructure.persistence.product.ProductQueryServiceImpl;
import org.bson.Document;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.domain.Sort;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class ProductQueryServiceImplTest {

    private final ProductRepository productRepository = Mockito.mock(ProductRepository.class);
    private final Mapper mapper = Mockito.mock(Mapper.class);
    private final MongoOperations mongoOperations = Mockito.mock(MongoOperations.class);
    private final ProductQueryServiceImpl service = new ProductQueryServiceImpl(productRepository, mapper, mongoOperations);

    @Test
    void shouldFindByIdReturningMappedOutput() {
        UUID productId = UUID.randomUUID();
        Category category = new Category("Electronics", true);
        Product product = Product.builder()
                .name("Notebook X11")
                .brand("Deep Diver")
                .regularPrice(new java.math.BigDecimal("1500.00"))
                .salePrice(new java.math.BigDecimal("1000.00"))
                .enabled(true)
                .category(category)
                .build();
        ProductDetailOutput output = ProductDetailOutputTestDataBuilder.aProduct()
                .id(productId)
                .build();

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(mapper.convert(product, ProductDetailOutput.class)).thenReturn(output);

        assertThat(service.findById(productId)).isEqualTo(output);
    }

    @Test
    void shouldThrowWhenProductDoesNotExist() {
        UUID productId = UUID.randomUUID();
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.findById(productId))
                .isInstanceOf(ProductNotFoundException.class);
    }

    @Test
    void shouldFilterReturningEmptyWhenNoProductsFound() {
        ProductFilter filter = new ProductFilter();
        filter.setPage(0);
        filter.setSize(10);

        when(mongoOperations.count(any(Query.class), eq(Product.class))).thenReturn(0L);

        var result = service.filter(filter);

        assertThat(result.getContent()).isEmpty();
        assertThat(result.getNumber()).isEqualTo(0);
        assertThat(result.getSize()).isEqualTo(0);
        assertThat(result.getTotalElements()).isEqualTo(0);
        assertThat(result.getTotalPages()).isEqualTo(0);
    }

    @Test
    void shouldFilterReturningProductsWhenFound() {
        ProductFilter filter = new ProductFilter();
        filter.setPage(0);
        filter.setSize(10);

        ProductSummaryOutput summaryOutput = ProductSummaryOutput.builder()
                .name("Notebook X11")
                .brand("Deep Diver")
                .build();

        AggregationResults<ProductSummaryOutput> aggregationResults =
                new AggregationResults<>(List.of(summaryOutput), new Document());

        when(mongoOperations.count(any(Query.class), eq(Product.class))).thenReturn(1L);
        when(mongoOperations.aggregate(any(Aggregation.class), eq(Product.class), eq(ProductSummaryOutput.class)))
                .thenReturn(aggregationResults);

        var result = service.filter(filter);

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getNumber()).isEqualTo(0);
        assertThat(result.getSize()).isEqualTo(10);
        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getTotalPages()).isEqualTo(1);
    }

    @Test
    void shouldFilterWithEnabledTrue() {
        ProductFilter filter = new ProductFilter();
        filter.setPage(0);
        filter.setSize(10);
        filter.setEnabled(true);

        when(mongoOperations.count(any(Query.class), eq(Product.class))).thenReturn(0L);

        var result = service.filter(filter);

        assertThat(result.getContent()).isEmpty();
    }

    @Test
    void shouldFilterWithEnabledFalse() {
        ProductFilter filter = new ProductFilter();
        filter.setPage(0);
        filter.setSize(10);
        filter.setEnabled(false);

        when(mongoOperations.count(any(Query.class), eq(Product.class))).thenReturn(0L);

        var result = service.filter(filter);

        assertThat(result.getContent()).isEmpty();
    }

    @Test
    void shouldFilterWithAddedAtFromAndTo() {
        ProductFilter filter = new ProductFilter();
        filter.setPage(0);
        filter.setSize(10);
        filter.setAddedAtFrom(OffsetDateTime.now().minusDays(10));
        filter.setAddedAtTo(OffsetDateTime.now());

        when(mongoOperations.count(any(Query.class), eq(Product.class))).thenReturn(0L);

        var result = service.filter(filter);

        assertThat(result.getContent()).isEmpty();
    }

    @Test
    void shouldFilterWithAddedAtFromOnly() {
        ProductFilter filter = new ProductFilter();
        filter.setPage(0);
        filter.setSize(10);
        filter.setAddedAtFrom(OffsetDateTime.now().minusDays(10));

        when(mongoOperations.count(any(Query.class), eq(Product.class))).thenReturn(0L);

        var result = service.filter(filter);

        assertThat(result.getContent()).isEmpty();
    }

    @Test
    void shouldFilterWithAddedAtToOnly() {
        ProductFilter filter = new ProductFilter();
        filter.setPage(0);
        filter.setSize(10);
        filter.setAddedAtTo(OffsetDateTime.now());

        when(mongoOperations.count(any(Query.class), eq(Product.class))).thenReturn(0L);

        var result = service.filter(filter);

        assertThat(result.getContent()).isEmpty();
    }

    @Test
    void shouldFilterWithPriceFromAndTo() {
        ProductFilter filter = new ProductFilter();
        filter.setPage(0);
        filter.setSize(10);
        filter.setPriceFrom(new BigDecimal("100"));
        filter.setPriceTo(new BigDecimal("500"));

        when(mongoOperations.count(any(Query.class), eq(Product.class))).thenReturn(0L);

        var result = service.filter(filter);

        assertThat(result.getContent()).isEmpty();
    }

    @Test
    void shouldFilterWithPriceFromOnly() {
        ProductFilter filter = new ProductFilter();
        filter.setPage(0);
        filter.setSize(10);
        filter.setPriceFrom(new BigDecimal("100"));

        when(mongoOperations.count(any(Query.class), eq(Product.class))).thenReturn(0L);

        var result = service.filter(filter);

        assertThat(result.getContent()).isEmpty();
    }

    @Test
    void shouldFilterWithPriceToOnly() {
        ProductFilter filter = new ProductFilter();
        filter.setPage(0);
        filter.setSize(10);
        filter.setPriceTo(new BigDecimal("500"));

        when(mongoOperations.count(any(Query.class), eq(Product.class))).thenReturn(0L);

        var result = service.filter(filter);

        assertThat(result.getContent()).isEmpty();
    }

    @Test
    void shouldFilterWithHasDiscountTrue() {
        ProductFilter filter = new ProductFilter();
        filter.setPage(0);
        filter.setSize(10);
        filter.setHasDiscount(true);

        when(mongoOperations.count(any(Query.class), eq(Product.class))).thenReturn(0L);

        var result = service.filter(filter);

        assertThat(result.getContent()).isEmpty();
    }

    @Test
    void shouldFilterWithHasDiscountFalse() {
        ProductFilter filter = new ProductFilter();
        filter.setPage(0);
        filter.setSize(10);
        filter.setHasDiscount(false);

        when(mongoOperations.count(any(Query.class), eq(Product.class))).thenReturn(0L);

        var result = service.filter(filter);

        assertThat(result.getContent()).isEmpty();
    }

    @Test
    void shouldFilterWithInStockTrue() {
        ProductFilter filter = new ProductFilter();
        filter.setPage(0);
        filter.setSize(10);
        filter.setInStock(true);

        when(mongoOperations.count(any(Query.class), eq(Product.class))).thenReturn(0L);

        var result = service.filter(filter);

        assertThat(result.getContent()).isEmpty();
    }

    @Test
    void shouldFilterWithInStockFalse() {
        ProductFilter filter = new ProductFilter();
        filter.setPage(0);
        filter.setSize(10);
        filter.setInStock(false);

        when(mongoOperations.count(any(Query.class), eq(Product.class))).thenReturn(0L);

        var result = service.filter(filter);

        assertThat(result.getContent()).isEmpty();
    }

    @Test
    void shouldFilterWithCategoriesId() {
        ProductFilter filter = new ProductFilter();
        filter.setPage(0);
        filter.setSize(10);
        filter.setCategoriesId(new UUID[]{UUID.randomUUID(), UUID.randomUUID()});

        when(mongoOperations.count(any(Query.class), eq(Product.class))).thenReturn(0L);

        var result = service.filter(filter);

        assertThat(result.getContent()).isEmpty();
    }

    @Test
    void shouldFilterWithTerm() {
        ProductFilter filter = new ProductFilter();
        filter.setPage(0);
        filter.setSize(10);
        filter.setTerm("notebook");

        when(mongoOperations.count(any(Query.class), eq(Product.class))).thenReturn(0L);

        var result = service.filter(filter);

        assertThat(result.getContent()).isEmpty();
    }

    @Test
    void shouldFilterWithAllCriteria() {
        ProductFilter filter = new ProductFilter();
        filter.setPage(0);
        filter.setSize(10);
        filter.setEnabled(true);
        filter.setAddedAtFrom(OffsetDateTime.now().minusDays(10));
        filter.setAddedAtTo(OffsetDateTime.now());
        filter.setPriceFrom(new BigDecimal("100"));
        filter.setPriceTo(new BigDecimal("500"));
        filter.setHasDiscount(true);
        filter.setInStock(true);
        filter.setCategoriesId(new UUID[]{UUID.randomUUID()});
        filter.setTerm("notebook");

        when(mongoOperations.count(any(Query.class), eq(Product.class))).thenReturn(0L);

        var result = service.filter(filter);

        assertThat(result.getContent()).isEmpty();
    }

    @Test
    void shouldFilterWithEmptyCategoriesId() {
        ProductFilter filter = new ProductFilter();
        filter.setPage(0);
        filter.setSize(10);
        filter.setCategoriesId(new UUID[]{});

        when(mongoOperations.count(any(Query.class), eq(Product.class))).thenReturn(0L);

        var result = service.filter(filter);

        assertThat(result.getContent()).isEmpty();
    }

    @Test
    void shouldFilterWithNullTerm() {
        ProductFilter filter = new ProductFilter();
        filter.setPage(0);
        filter.setSize(10);
        filter.setTerm("");

        when(mongoOperations.count(any(Query.class), eq(Product.class))).thenReturn(0L);

        var result = service.filter(filter);

        assertThat(result.getContent()).isEmpty();
    }

    @Test
    void shouldFilterWithSalePriceSortType() {
        ProductFilter filter = new ProductFilter();
        filter.setPage(0);
        filter.setSize(10);
        filter.setSortByProperty(ProductFilter.SortType.SALE_PRICE);
        filter.setSortDirection(Sort.Direction.DESC);

        when(mongoOperations.count(any(Query.class), eq(Product.class))).thenReturn(0L);

        var result = service.filter(filter);

        assertThat(result.getContent()).isEmpty();
    }

    @Test
    void shouldFilterWithDefaultSortType() {
        ProductFilter filter = new ProductFilter();
        filter.setPage(0);
        filter.setSize(10);

        when(mongoOperations.count(any(Query.class), eq(Product.class))).thenReturn(0L);

        var result = service.filter(filter);

        assertThat(result.getContent()).isEmpty();
    }

    @Test
    void shouldCalculateMultiplePages() {
        ProductFilter filter = new ProductFilter();
        filter.setPage(0);
        filter.setSize(10);

        AggregationResults<ProductSummaryOutput> aggregationResults =
                new AggregationResults<>(Collections.emptyList(), new Document());

        when(mongoOperations.count(any(Query.class), eq(Product.class))).thenReturn(25L);
        when(mongoOperations.aggregate(any(Aggregation.class), eq(Product.class), eq(ProductSummaryOutput.class)))
                .thenReturn(aggregationResults);

        var result = service.filter(filter);

        assertThat(result.getTotalPages()).isEqualTo(3);
        assertThat(result.getTotalElements()).isEqualTo(25);
    }

    @Test
    void shouldFilterWithTermAndReturnResults() {
        ProductFilter filter = new ProductFilter();
        filter.setPage(0);
        filter.setSize(10);
        filter.setTerm("notebook");

        ProductSummaryOutput summaryOutput = ProductSummaryOutput.builder()
                .name("Notebook X11")
                .brand("Deep Diver")
                .score(1.5f)
                .build();

        AggregationResults<ProductSummaryOutput> aggregationResults =
                new AggregationResults<>(List.of(summaryOutput), new Document());

        when(mongoOperations.count(any(Query.class), eq(Product.class))).thenReturn(1L);
        when(mongoOperations.aggregate(any(Aggregation.class), eq(Product.class), eq(ProductSummaryOutput.class)))
                .thenReturn(aggregationResults);

        var result = service.filter(filter);

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getTotalPages()).isEqualTo(1);
    }

    @Test
    void shouldCreateTextScoreDocument() {
        Document doc = ProductQueryServiceImpl.textScoreDocument();

        assertThat(doc).isNotNull();
        assertThat(doc.containsKey("$addFields")).isTrue();
    }

    @Test
    void shouldFilterWithTermAndCriteriaAndReturnResults() {
        ProductFilter filter = new ProductFilter();
        filter.setPage(0);
        filter.setSize(10);
        filter.setTerm("notebook");
        filter.setEnabled(true);
        filter.setHasDiscount(true);
        filter.setInStock(true);

        ProductSummaryOutput summaryOutput = ProductSummaryOutput.builder()
                .name("Notebook X11")
                .brand("Deep Diver")
                .score(1.5f)
                .build();

        AggregationResults<ProductSummaryOutput> aggregationResults =
                new AggregationResults<>(List.of(summaryOutput), new Document());

        when(mongoOperations.count(any(Query.class), eq(Product.class))).thenReturn(1L);
        when(mongoOperations.aggregate(any(Aggregation.class), eq(Product.class), eq(ProductSummaryOutput.class)))
                .thenReturn(aggregationResults);

        var result = service.filter(filter);

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getTotalElements()).isEqualTo(1);
    }

    @Test
    void shouldFilterWithCriteriaAndReturnResults() {
        ProductFilter filter = new ProductFilter();
        filter.setPage(0);
        filter.setSize(10);
        filter.setEnabled(true);
        filter.setHasDiscount(true);
        filter.setInStock(true);

        ProductSummaryOutput summaryOutput = ProductSummaryOutput.builder()
                .name("Notebook X11")
                .brand("Deep Diver")
                .build();

        AggregationResults<ProductSummaryOutput> aggregationResults =
                new AggregationResults<>(List.of(summaryOutput), new Document());

        when(mongoOperations.count(any(Query.class), eq(Product.class))).thenReturn(1L);
        when(mongoOperations.aggregate(any(Aggregation.class), eq(Product.class), eq(ProductSummaryOutput.class)))
                .thenReturn(aggregationResults);

        var result = service.filter(filter);

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getTotalElements()).isEqualTo(1);
    }
}
