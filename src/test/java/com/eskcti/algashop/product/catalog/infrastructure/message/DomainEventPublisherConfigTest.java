package com.eskcti.algashop.product.catalog.infrastructure.message;

import com.eskcti.algashop.product.catalog.domain.model.DomainEventPublisher;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationEventPublisher;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class DomainEventPublisherConfigTest {

    private final ApplicationEventPublisher applicationEventPublisher = mock(ApplicationEventPublisher.class);
    private final DomainEventPublisherConfig config = new DomainEventPublisherConfig();

    @Test
    void shouldCreateDomainEventPublisherBean() {
        DomainEventPublisher publisher = config.domainEventPublisher(applicationEventPublisher);

        assertThat(publisher).isNotNull();
    }

    @Test
    void shouldDelegateToApplicationEventPublisher() {
        DomainEventPublisher publisher = config.domainEventPublisher(applicationEventPublisher);

        Object event = new Object();
        publisher.publish(event);

        verify(applicationEventPublisher).publishEvent(event);
    }
}
