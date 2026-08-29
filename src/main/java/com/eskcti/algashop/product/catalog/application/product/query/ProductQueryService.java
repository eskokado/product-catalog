package com.eskcti.algashop.product.catalog.application.product.query;

import java.util.UUID;

import com.eskcti.algashop.product.catalog.application.PageModel;

public interface ProductQueryService {
    ProductDetailOutput findById(UUID productId);

    PageModel<ProductSummaryOutput> filter(ProductFilter filter);
}
