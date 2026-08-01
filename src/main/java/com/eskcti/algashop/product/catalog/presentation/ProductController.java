package com.eskcti.algashop.product.catalog.presentation;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.eskcti.algashop.product.catalog.application.product.management.ProductInput;
import com.eskcti.algashop.product.catalog.application.product.management.ProductManagementApplicationService;
import com.eskcti.algashop.product.catalog.application.product.query.PageModel;
import com.eskcti.algashop.product.catalog.application.product.query.ProductDetailOutput;
import com.eskcti.algashop.product.catalog.application.product.query.ProductQueryService;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductQueryService productQueryService;
    private final ProductManagementApplicationService productManagementApplicationService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductDetailOutput create(@RequestBody @Valid ProductInput input) {
        UUID productId = productManagementApplicationService.create(input);
        return productQueryService.findById(productId);
    }

    @GetMapping("/{productId}")
    public ProductDetailOutput findById(@PathVariable UUID productId) {
        return productQueryService.findById(productId);
    }

    @GetMapping
    public PageModel<ProductDetailOutput> filter(
            @RequestParam(name = "size", required = false) Integer size,
            @RequestParam(name = "number", required = false) Integer number) {
        return productQueryService.filter(size, number);
    }

}
