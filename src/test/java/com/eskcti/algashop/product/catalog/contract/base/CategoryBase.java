package com.eskcti.algashop.product.catalog.contract.base;

import com.eskcti.algashop.product.catalog.application.PageModel;
import com.eskcti.algashop.product.catalog.application.category.management.CategoryInput;
import com.eskcti.algashop.product.catalog.application.category.management.CategoryManagementService;
import com.eskcti.algashop.product.catalog.application.category.query.CategoryDetailOutput;
import com.eskcti.algashop.product.catalog.application.category.query.CategoryFilter;
import com.eskcti.algashop.product.catalog.application.category.query.CategoryOutputTestDataBuilder;
import com.eskcti.algashop.product.catalog.application.category.query.CategoryQueryService;
import com.eskcti.algashop.product.catalog.presentation.CategoryController;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.operation.preprocess.Preprocessors;
import org.springframework.restdocs.templates.TemplateFormats;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;

@WebMvcTest(controllers = CategoryController.class)
@ExtendWith(RestDocumentationExtension.class)
public class CategoryBase {

        @Autowired
        private WebApplicationContext context;

        @MockitoBean
        private CategoryQueryService categoryQueryService;

        @MockitoBean
        private CategoryManagementService categoryManagementService;

        public static final UUID validCategoryId = UUID.fromString("f5ab7a1e-37da-41e1-892b-a1d38275c2f2");

        public static final UUID createdCategoryId = UUID.randomUUID();

        @BeforeEach
        void setUp(RestDocumentationContextProvider documentationContextProvider) {
                RestAssuredMockMvc.mockMvc(MockMvcBuilders.webAppContextSetup(context)
                                .apply(documentationConfiguration(documentationContextProvider)
                                                .snippets().withTemplateFormat(TemplateFormats.asciidoctor())
                                                .and().operationPreprocessors()
                                                .withResponseDefaults(Preprocessors.prettyPrint()))
                                .alwaysDo(MockMvcRestDocumentation.document("{ClassName}/{methodName}"))
                                .defaultResponseCharacterEncoding(StandardCharsets.UTF_8)
                                .build());

                RestAssuredMockMvc.enableLoggingOfRequestAndResponseIfValidationFails();

                Mockito.when(categoryQueryService.filter(Mockito.any(CategoryFilter.class)))
                                .then((answer) -> {
                                        CategoryFilter filter = answer.getArgument(0);
                                        return PageModel.<CategoryDetailOutput>builder()
                                                        .number(0)
                                                        .size(filter.getSize())
                                                        .totalPages(1)
                                                        .totalElements(2)
                                                        .content(
                                                                        List.of(
                                                                                        CategoryOutputTestDataBuilder
                                                                                                        .aCategory()
                                                                                                        .build(),
                                                                                        CategoryOutputTestDataBuilder
                                                                                                        .aDisabledCategory()
                                                                                                        .build()))
                                                        .build();
                                });

                Mockito.when(categoryQueryService.findById(validCategoryId))
                                .thenReturn(CategoryOutputTestDataBuilder.aCategory().id(validCategoryId).build());

                Mockito.when(categoryManagementService.create(Mockito.any(CategoryInput.class)))
                                .thenReturn(createdCategoryId);

                Mockito.when(categoryQueryService.findById(createdCategoryId))
                                .thenReturn(CategoryOutputTestDataBuilder.aCategory().id(createdCategoryId).build());
        }
}
