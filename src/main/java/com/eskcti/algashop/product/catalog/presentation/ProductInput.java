package com.eskcti.algashop.product.catalog.presentation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductInput {
    @NotBlank
    private String name;

    @NotBlank
    private String brand;

    @NotNull
    private BigDecimal regularPrice;

    @NotNull
    private BigDecimal salePrice;

    @NotNull
    private Boolean enabled;

    @NotNull
    private UUID categoryId;

    private String description;
}
