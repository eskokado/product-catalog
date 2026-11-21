package com.eskcti.algashop.product.catalog.presentation;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.eskcti.algashop.product.catalog.application.PageModel;
import com.eskcti.algashop.product.catalog.application.product.management.ProductInput;
import com.eskcti.algashop.product.catalog.application.product.management.ProductManagementApplicationService;
import com.eskcti.algashop.product.catalog.application.product.query.ProductDetailOutput;
import com.eskcti.algashop.product.catalog.application.product.query.ProductSummaryOutput;
import com.eskcti.algashop.product.catalog.application.product.query.ProductQueryService;
import com.eskcti.algashop.product.catalog.application.product.query.ProductFilter;
import com.eskcti.algashop.product.catalog.domain.model.category.CategoryNotFoundException;
import com.eskcti.algashop.product.catalog.infrastructure.security.SecurityAnnotations.CanReadProducts;
import com.eskcti.algashop.product.catalog.infrastructure.security.SecurityAnnotations.CanWriteProducts;
import com.eskcti.algashop.product.catalog.infrastructure.security.SecurityAnnotations.CanWriteProductsStock;

import java.time.Duration;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import static com.eskcti.algashop.product.catalog.infrastructure.security.SecurityAnnotations.*;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
@Slf4j
public class ProductController {

    private final ProductQueryService productQueryService;
    private final ProductManagementApplicationService productManagementApplicationService;

    private final Map<UUID, AtomicInteger> simulatedFailures = new ConcurrentHashMap<>();

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @CanWriteProducts
    public ProductDetailOutput create(@RequestBody @Valid ProductInput input) {
        try {
            return productManagementApplicationService.create(input);
        } catch (CategoryNotFoundException e) {
            throw new UnprocessableContentException(e.getMessage(), e);
        }
    }

    @GetMapping("/{productId}")
    @CanReadProducts
    public ResponseEntity<ProductDetailOutput> findById(
            @PathVariable UUID productId,
            @RequestParam(defaultValue = "0") int failFirstAttempts,
            @RequestParam(defaultValue = "0") long delayMs) throws InterruptedException {

        if (delayMs > 0) {
            log.warn("Simulating a {}ms delay for product {}", delayMs, productId);
            Thread.sleep(delayMs);
        }

        if (shouldSimulateFailure(productId, failFirstAttempts)) {
            log.warn("Simulating a failure for product {}", productId);
            throw new RuntimeException("Fake exception");
        }

        ProductDetailOutput product = productQueryService.findById(productId);
        return ResponseEntity.ok()
                .cacheControl(CacheControl.maxAge(Duration.ofMinutes(1)).cachePublic())
                .eTag("product:id:" + product.getId() + ":v:" + product.getVersion())
                .lastModified(product.getUpdatedAt().toInstant())
                .body(product);
    }

    private boolean shouldSimulateFailure(UUID productId, int failFirstAttempts) {
        AtomicInteger consecutiveFailures = simulatedFailures.computeIfAbsent(productId, id -> new AtomicInteger());

        if (failFirstAttempts <= 0) {
            consecutiveFailures.set(0);
            return false;
        }

        if (consecutiveFailures.get() < failFirstAttempts) {
            consecutiveFailures.incrementAndGet();
            return true;
        }

        consecutiveFailures.set(0);
        return false;
    }

    @PutMapping("/{productId}")
    @CanWriteProducts
    public ProductDetailOutput update(@PathVariable UUID productId,
            @RequestBody @Valid ProductInput input) {
        return productManagementApplicationService.update(productId, input);
    }

    @DeleteMapping("/{productId}/enable")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CanWriteProducts
    public void disable(@PathVariable UUID productId) {
        productManagementApplicationService.disable(productId);
    }

    @PutMapping("/{productId}/enable")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CanWriteProducts
    public void enable(@PathVariable UUID productId) {
        productManagementApplicationService.enable(productId);
    }

    @GetMapping
    @CanReadProducts
    public PageModel<ProductSummaryOutput> filter(ProductFilter productFilter) {
        return productQueryService.filter(productFilter);
    }

    @PostMapping("/{productId}/restock")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CanWriteProductsStock
    public void restock(@PathVariable UUID productId, @RequestBody @Valid ProductQuantityModel productQuantityModel) {
        productManagementApplicationService.restock(productId, productQuantityModel.getQuantity());
    }

    @PostMapping("/{productId}/withdraw")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CanWriteProductsStock
    public void withdraw(@PathVariable UUID productId, @RequestBody @Valid ProductQuantityModel productQuantityModel) {
        productManagementApplicationService.withdraw(productId, productQuantityModel.getQuantity());
    }

}
