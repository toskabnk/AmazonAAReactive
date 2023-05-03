package com.svalero.AmazonAAReactive.repository;

import com.svalero.AmazonAAReactive.domain.Product;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface ProductRepository extends ReactiveMongoRepository<Product, String> {
    @Override
    Flux<Product> findAll();
    Flux<Product> findByNameContaining(String name);
    Flux<Product> findByCategory(String category);
    Flux<Product> findByDescriptionContainingIgnoreCase(String description);
    Mono<Product> findById(String id);

}
