package com.eskcti.algashop.product.catalog;

import com.eskcti.algashop.product.catalog.infrastructure.security.ProductCatalogSecurityConfig;
import com.eskcti.algashop.product.catalog.utils.MockJwtDecoderConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static com.eskcti.algashop.product.catalog.utils.MockJwtDecoderFactory.DEFAULT_TOKEN_VALUE;

/**
 * Base dos testes de integracao do lado web.
 *
 * <p>O slice {@code @WebMvcTest} nao scaneia {@link ProductCatalogSecurityConfig}, entao o
 * {@code @PreAuthorize} de {@code SecurityAnnotations} era avaliado pelo method security padrao
 * do Boot contra o usuario anonimo e devolvia 403. Importar a config real conecta o filter chain
 * do resource server aos escopos do token; o {@link MockJwtDecoderConfig} entrega um
 * {@code JwtDecoder} deterministico, sem falar com o authorization-server.</p>
 */
@Import({ProductCatalogSecurityConfig.class, MockJwtDecoderConfig.class})
public abstract class AbstractControllerIT {

    @Autowired
    protected MockMvc mockMvc;

    protected MockHttpServletRequestBuilder authenticated(MockHttpServletRequestBuilder request) {
        return request.header("Authorization", "Bearer " + DEFAULT_TOKEN_VALUE);
    }

}
