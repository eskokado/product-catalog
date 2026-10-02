package com.eskcti.algashop.product.catalog.utils;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.mongodb.MongoDBContainer;

/**
 * Container MongoDB (replSet rs0) compartilhado por toda a JVM de teste.
 *
 * <p>O bean declara um destroy method que nao pare o container: com o
 * {@code TestcontainersLifecycleBeanPostProcessor} do Spring Boot chamando {@code close()} na
 * destruicao de qualquer contexto, o primeiro contexto destruido derrubaria o container e
 * invalidaria as portas ja entregues aos demais contextos.</p>
 */
@TestConfiguration
public class TestcontainerMongoDBConfig {

    private static final SharedMongoDBContainer MONGODB_CONTAINER =
            new SharedMongoDBContainer("mongo:8");

    static {
        MONGODB_CONTAINER.withCommand("--replSet", "rs0");
        MONGODB_CONTAINER.start();

        try {
            MONGODB_CONTAINER.execInContainer(
                    "mongosh",
                    "--eval",
                    """
                    rs.initiate({
                        _id: "rs0",
                        members: [
                            {
                                _id: 0,
                                host: "localhost:27017"
                            }
                        ]
                    })
                    """
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Bean(destroyMethod = "keepAlive")
    @ServiceConnection
    public MongoDBContainer mongoDBContainer() {
        if (!MONGODB_CONTAINER.isRunning()) {
            MONGODB_CONTAINER.start();
        }
        return MONGODB_CONTAINER;
    }

    public static final class SharedMongoDBContainer extends MongoDBContainer {

        SharedMongoDBContainer(String dockerImageName) {
            super(dockerImageName);
        }

        public void keepAlive() {
            // intencionalmente vazio: o container e compartilhado por todos os contextos
            // da JVM de teste e so deve ser encerrado junto com ela (Ryuk).
        }

    }

}
