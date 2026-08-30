package com.eskcti.algashop.product.catalog.application.category.query;

import java.util.UUID;

import com.eskcti.algashop.product.catalog.application.PageModel;

public interface CategoryQueryService {
    PageModel<CategoryDetailOutput> filter(CategoryFilter filter);

    CategoryDetailOutput findById(UUID categoryId);
}
