package com.eskcti.algashop.product.catalog.domain.model.product;

import com.eskcti.algashop.product.catalog.infrastructure.persistence.MongoConfig;
import com.eskcti.algashop.product.catalog.infrastructure.persistence.dataload.DataLoadProperties;
import com.eskcti.algashop.product.catalog.infrastructure.persistence.dataload.DataLoader;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.DefaultApplicationArguments;
import org.springframework.boot.data.mongodb.test.autoconfigure.DataMongoTest;
import org.springframework.context.annotation.Import;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@DataMongoTest
@Import({
        MongoConfig.class,
        DataLoader.class,
        DataLoadProperties.class
})
class StockMovementRepositoryIT {

    @Autowired
    private StockMovementRepository stockMovementRepository;

    @Autowired
    private DataLoader dataLoader;

    @BeforeEach
    public void beforeEach() throws Exception {
        stockMovementRepository.deleteAll();
        dataLoader.run(new DefaultApplicationArguments());
    }

    @Test
    public void shouldSaveStockMovement() {
        UUID productId = UUID.randomUUID();
        StockMovement movement = StockMovement.builder()
                .productId(productId)
                .movementQuantity(10)
                .previousQuantity(0)
                .newQuantity(10)
                .type(StockMovement.MovementType.STOCK_IN)
                .build();

        StockMovement saved = stockMovementRepository.save(movement);

        Assertions.assertThat(saved.getId()).isNotNull();
        Assertions.assertThat(saved.getProductId()).isEqualTo(productId);
        Assertions.assertThat(saved.getMovementQuantity()).isEqualTo(10);
        Assertions.assertThat(saved.getPreviousQuantity()).isEqualTo(0);
        Assertions.assertThat(saved.getNewQuantity()).isEqualTo(10);
        Assertions.assertThat(saved.getType()).isEqualTo(StockMovement.MovementType.STOCK_IN);
    }

    @Test
    public void shouldFindStockMovementById() {
        UUID productId = UUID.randomUUID();
        StockMovement movement = StockMovement.builder()
                .productId(productId)
                .movementQuantity(5)
                .previousQuantity(10)
                .newQuantity(15)
                .type(StockMovement.MovementType.STOCK_IN)
                .build();

        StockMovement saved = stockMovementRepository.save(movement);

        Optional<StockMovement> found = stockMovementRepository.findById(saved.getId());

        Assertions.assertThat(found).isPresent();
        Assertions.assertThat(found.get().getProductId()).isEqualTo(productId);
        Assertions.assertThat(found.get().getMovementQuantity()).isEqualTo(5);
    }

    @Test
    public void shouldFindAllStockMovements() {
        UUID productId = UUID.randomUUID();

        stockMovementRepository.save(StockMovement.builder()
                .productId(productId)
                .movementQuantity(10)
                .previousQuantity(0)
                .newQuantity(10)
                .type(StockMovement.MovementType.STOCK_IN)
                .build());

        stockMovementRepository.save(StockMovement.builder()
                .productId(productId)
                .movementQuantity(5)
                .previousQuantity(10)
                .newQuantity(5)
                .type(StockMovement.MovementType.STOCK_OUT)
                .build());

        List<StockMovement> movements = stockMovementRepository.findAll();

        Assertions.assertThat(movements).hasSize(2);
    }

    @Test
    public void shouldDeleteStockMovement() {
        UUID productId = UUID.randomUUID();
        StockMovement movement = StockMovement.builder()
                .productId(productId)
                .movementQuantity(10)
                .previousQuantity(0)
                .newQuantity(10)
                .type(StockMovement.MovementType.STOCK_IN)
                .build();

        StockMovement saved = stockMovementRepository.save(movement);
        UUID savedId = saved.getId();

        stockMovementRepository.deleteById(savedId);

        Optional<StockMovement> found = stockMovementRepository.findById(savedId);
        Assertions.assertThat(found).isEmpty();
    }

    @Test
    public void shouldCountStockMovements() {
        UUID productId = UUID.randomUUID();

        stockMovementRepository.save(StockMovement.builder()
                .productId(productId)
                .movementQuantity(10)
                .previousQuantity(0)
                .newQuantity(10)
                .type(StockMovement.MovementType.STOCK_IN)
                .build());

        long count = stockMovementRepository.count();

        Assertions.assertThat(count).isEqualTo(1);
    }
}
