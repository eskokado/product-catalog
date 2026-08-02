package com.eskcti.algashop.product.catalog.application.category.query;

import com.eskcti.algashop.product.catalog.application.PageModel;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CategoryQueryServiceImpl implements CategoryQueryService {

    @Override
    public PageModel<CategoryDetailOutput> filter(Integer size, Integer number) {
        return null;
    }

    @Override
    public CategoryDetailOutput findById(UUID categoryId) {
        return null;
    }
}
