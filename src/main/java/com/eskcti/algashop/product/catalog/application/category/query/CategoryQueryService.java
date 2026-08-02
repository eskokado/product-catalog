package com.eskcti.algashop.product.catalog.application.category.query;

import java.util.UUID;

import com.eskcti.algashop.product.catalog.application.PageModel;

public interface CategoryQueryService {
    PageModel<CategoryDetailOutput> filter(Integer size, Integer number);

    CategoryDetailOutput findById(UUID categoryId);
}
