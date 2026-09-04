package com.eskcti.algashop.product.catalog.domain.model;

public interface DomainEventPublisher {
    void publish(Object event);
}
