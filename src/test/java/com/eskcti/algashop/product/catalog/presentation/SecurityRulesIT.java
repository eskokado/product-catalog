package com.eskcti.algashop.product.catalog.presentation;

import com.eskcti.algashop.product.catalog.application.category.management.CategoryManagementService;
import com.eskcti.algashop.product.catalog.application.category.query.CategoryDetailOutput;
import com.eskcti.algashop.product.catalog.application.category.query.CategoryQueryService;
import com.eskcti.algashop.product.catalog.application.product.management.ProductManagementApplicationService;
import com.eskcti.algashop.product.catalog.application.product.query.ProductQueryService;
import com.eskcti.algashop.product.catalog.AbstractControllerIT;
import com.eskcti.algashop.product.catalog.application.upload.UploadRequestApplicationService;
import com.eskcti.algashop.product.catalog.infrastructure.storage.StorageProviderStub;
import org.junit.jupiter.api.Test;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {
        UploadRequestController.class,
        ProductController.class,
        CategoryController.class
})
@Import({
        UploadRequestApplicationService.class,
        StorageProviderStub.class
})
class SecurityRulesIT extends AbstractControllerIT {

    private static final String UPLOAD_REQUEST_BODY = """
            {
              "originalFileName": "photo.png",
              "contentLength": 2048
            }
            """;

    private static final String CATEGORY_BODY = """
            {
              "name": "Notebook",
              "enabled": true
            }
            """;

    private static final String STOCK_BODY = """
            {
              "quantity": 10
            }
            """;

    @MockitoBean
    private ProductQueryService productQueryService;

    @MockitoBean
    private ProductManagementApplicationService productManagementApplicationService;

    @MockitoBean
    private CategoryQueryService categoryQueryService;

    @MockitoBean
    private CategoryManagementService categoryManagementService;

    @Test
    @WithMockUser(authorities = { "SCOPE_products:write", "ROLE_CUSTOMER" })
    void shouldForbidCustomerFromWritingProductsEvenWithScope() throws Exception {
        mockMvc.perform(post("/api/v1/upload-requests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(UPLOAD_REQUEST_BODY))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(authorities = { "SCOPE_products:write" })
    void shouldForbidWritingProductsWithoutRole() throws Exception {
        mockMvc.perform(post("/api/v1/upload-requests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(UPLOAD_REQUEST_BODY))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(authorities = { "SCOPE_products:write", "ROLE_OPERATOR" })
    void shouldAllowOperatorToWriteProducts() throws Exception {
        mockMvc.perform(post("/api/v1/upload-requests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(UPLOAD_REQUEST_BODY))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(authorities = { "SCOPE_categories:write", "ROLE_CUSTOMER" })
    void shouldForbidCustomerFromWritingCategoriesEvenWithScope() throws Exception {
        mockMvc.perform(post("/api/v1/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(CATEGORY_BODY))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(authorities = { "SCOPE_categories:write", "ROLE_OPERATOR" })
    void shouldAllowOperatorToWriteCategories() throws Exception {
        UUID categoryId = UUID.randomUUID();
        when(categoryManagementService.create(any())).thenReturn(categoryId);
        when(categoryQueryService.findById(categoryId))
                .thenReturn(CategoryDetailOutput.builder().id(categoryId).name("Notebook").enabled(true).build());

        mockMvc.perform(post("/api/v1/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(CATEGORY_BODY))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(authorities = { "SCOPE_products:stock:write", "ROLE_OPERATOR" })
    void shouldForbidOperatorFromWritingStock() throws Exception {
        mockMvc.perform(post("/api/v1/products/{productId}/restock", UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(STOCK_BODY))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(authorities = { "SCOPE_products:stock:write", "ROLE_MANAGER" })
    void shouldAllowManagerToWriteStock() throws Exception {
        mockMvc.perform(post("/api/v1/products/{productId}/restock", UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(STOCK_BODY))
                .andExpect(status().isNoContent());
    }
}
