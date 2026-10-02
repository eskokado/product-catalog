package com.eskcti.algashop.product.catalog;

import com.eskcti.algashop.product.catalog.utils.MockJwtDecoderConfig;
import com.eskcti.algashop.product.catalog.utils.TestcontainerMongoDBConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@SpringBootTest
@Import({ TestcontainerMongoDBConfig.class, MockJwtDecoderConfig.class })
class ProductCatalogApplicationIT {

	@Test
	void contextLoads() {
	}

}
