package com.eskcti.algashop.product.catalog.application.utility;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class PageFilter {
  @Builder.Default
  private int size = 15;
  @Builder.Default
  private int page = 0;
}