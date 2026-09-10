package com.eskcti.algashop.product.catalog.application.product.query;

import java.util.UUID;

import com.eskcti.algashop.product.catalog.application.PageModel;
import org.springframework.cache.annotation.Cacheable;

public interface ProductQueryService {
    @Cacheable(cacheNames = "algashop:products:v1", key = "#productId")
    ProductDetailOutput findById(UUID productId);

    PageModel<ProductSummaryOutput> filter(ProductFilter filter);
}
