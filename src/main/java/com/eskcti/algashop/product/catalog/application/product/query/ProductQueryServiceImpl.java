package com.eskcti.algashop.product.catalog.application.product.query;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ProductQueryServiceImpl implements ProductQueryService {

    @Override
    public ProductDetailOutput findById(UUID productId) {
        return null;
    }

    @Override
    public PageModel<ProductDetailOutput> filter(Integer size, Integer number) {
        return null;
    }
}
