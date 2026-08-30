package com.eskcti.algashop.product.catalog.infrastructure.persistence.dataload;

import com.eskcti.algashop.product.catalog.infrastructure.utility.AlgaShopResourceUtils;
import org.bson.BsonDocument;
import org.bson.Document;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import com.mongodb.client.MongoCollection;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class DataLoaderTest {

    private final MongoOperations mongoOperations = Mockito.mock(MongoOperations.class);
    private final DataLoadProperties properties = new DataLoadProperties();

    @Test
    void shouldNotLoadDataWhenDisabled() throws Exception {
        properties.setEnabled(false);
        DataLoader loader = new DataLoader(mongoOperations, properties);

        loader.run(null);

        Mockito.verifyNoInteractions(mongoOperations);
    }

    @Test
    void shouldNotLoadDataWhenSourcesIsNull() throws Exception {
        properties.setEnabled(true);
        properties.setAutoDelete(false);
        properties.setSources(null);
        DataLoader loader = new DataLoader(mongoOperations, properties);

        loader.run(null);

        Mockito.verifyNoInteractions(mongoOperations);
    }

    @Test
    void shouldNotLoadDataWhenSourcesIsEmpty() throws Exception {
        properties.setEnabled(true);
        properties.setAutoDelete(false);
        properties.setSources(Collections.emptyList());
        DataLoader loader = new DataLoader(mongoOperations, properties);

        loader.run(null);

        Mockito.verifyNoInteractions(mongoOperations);
    }

    @Test
    void shouldImportValidJsonFile() throws Exception {
        properties.setEnabled(true);
        properties.setAutoDelete(false);
        DataLoadProperties.DataLoadSource source = new DataLoadProperties.DataLoadSource();
        source.setLocation("db/testdata/categories.json");
        source.setCollection("categories");
        properties.setSources(List.of(source));

        Document doc = new Document("name", "Laptops");
        List<Document> docs = List.of(doc);
        doReturn(docs).when(mongoOperations).insert(anyList(), eq("categories"));

        DataLoader loader = new DataLoader(mongoOperations, properties);
        loader.run(null);

        verify(mongoOperations).insert(anyList(), eq("categories"));
    }

    @Test
    void shouldSkipBlankResource() throws Exception {
        properties.setEnabled(true);
        properties.setAutoDelete(false);
        DataLoadProperties.DataLoadSource source = new DataLoadProperties.DataLoadSource();
        source.setLocation("empty.json");
        source.setCollection("test");
        properties.setSources(List.of(source));

        try (MockedStatic<AlgaShopResourceUtils> utils = Mockito.mockStatic(AlgaShopResourceUtils.class)) {
            utils.when(() -> AlgaShopResourceUtils.readContent("empty.json")).thenReturn("  ");

            DataLoader loader = new DataLoader(mongoOperations, properties);
            loader.run(null);

            verifyNoInteractions(mongoOperations);
        }
    }

    @Test
    void shouldHandleParseErrorGracefully() throws Exception {
        properties.setEnabled(true);
        properties.setAutoDelete(false);
        DataLoadProperties.DataLoadSource source = new DataLoadProperties.DataLoadSource();
        source.setLocation("invalid.json");
        source.setCollection("test");
        properties.setSources(List.of(source));

        try (MockedStatic<AlgaShopResourceUtils> utils = Mockito.mockStatic(AlgaShopResourceUtils.class)) {
            utils.when(() -> AlgaShopResourceUtils.readContent("invalid.json")).thenReturn("not valid json {{{");

            DataLoader loader = new DataLoader(mongoOperations, properties);
            loader.run(null);

            verify(mongoOperations, never()).insert(anyList(), anyString());
        }
    }

    @Test
    void shouldHandleInsertErrorGracefully() throws Exception {
        properties.setEnabled(true);
        properties.setAutoDelete(false);
        DataLoadProperties.DataLoadSource source = new DataLoadProperties.DataLoadSource();
        source.setLocation("db/testdata/categories.json");
        source.setCollection("categories");
        properties.setSources(List.of(source));

        when(mongoOperations.insert(anyList(), eq("categories")))
                .thenThrow(new RuntimeException("DB error"));

        DataLoader loader = new DataLoader(mongoOperations, properties);
        loader.run(null);

        verify(mongoOperations).insert(anyList(), eq("categories"));
    }

    @SuppressWarnings("unchecked")
    @Test
    void shouldDropCollectionWhenAutoDropEnabled() throws Exception {
        properties.setEnabled(true);
        properties.setAutoDelete(true);
        DataLoadProperties.DataLoadSource source = new DataLoadProperties.DataLoadSource();
        source.setLocation("db/testdata/categories.json");
        source.setCollection("categories");
        properties.setSources(List.of(source));

        MongoCollection<Document> mongoCollection = Mockito.mock(MongoCollection.class);
        when(mongoOperations.getCollection("categories")).thenReturn(mongoCollection);
        doReturn(List.of()).when(mongoOperations).insert(anyList(), eq("categories"));

        DataLoader loader = new DataLoader(mongoOperations, properties);
        loader.run(null);

        verify(mongoCollection).deleteMany(org.mockito.ArgumentMatchers.any(BsonDocument.class));
        verify(mongoOperations).insert(anyList(), eq("categories"));
    }

    @Test
    void shouldImportMultipleSources() throws Exception {
        properties.setEnabled(true);
        properties.setAutoDelete(false);

        DataLoadProperties.DataLoadSource source1 = new DataLoadProperties.DataLoadSource();
        source1.setLocation("db/testdata/categories.json");
        source1.setCollection("categories");

        DataLoadProperties.DataLoadSource source2 = new DataLoadProperties.DataLoadSource();
        source2.setLocation("db/testdata/products.json");
        source2.setCollection("products");

        properties.setSources(List.of(source1, source2));

        Document doc = new Document("name", "test");
        doReturn(List.of(doc)).when(mongoOperations).insert(anyList(), eq("categories"));
        doReturn(List.of(doc)).when(mongoOperations).insert(anyList(), eq("products"));

        DataLoader loader = new DataLoader(mongoOperations, properties);
        loader.run(null);

        verify(mongoOperations).insert(anyList(), eq("categories"));
        verify(mongoOperations).insert(anyList(), eq("products"));
    }

    @Test
    void shouldReturnZeroWhenDocumentsListIsNull() throws Exception {
        properties.setEnabled(true);
        properties.setAutoDelete(false);
        DataLoader loader = new DataLoader(mongoOperations, properties);

        Method insertIntoMethod = DataLoader.class.getDeclaredMethod("insertInto", List.class, String.class);
        insertIntoMethod.setAccessible(true);

        int result = (int) insertIntoMethod.invoke(loader, (Object) null, "test");

        assertThat(result).isZero();
        verifyNoInteractions(mongoOperations);
    }
}
