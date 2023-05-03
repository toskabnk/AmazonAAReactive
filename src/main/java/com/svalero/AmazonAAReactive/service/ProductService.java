package com.svalero.AmazonAAReactive.service;

import com.svalero.AmazonAAReactive.domain.Product;
import com.svalero.AmazonAAReactive.exception.ProductNotFoundException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductService {
    Flux<Product> findAll();
    Mono<Product> findById(String id) throws ProductNotFoundException;
    Flux<Product> findByNameContaining(String name);
    Flux<Product> findByCategory(String category);
    Flux<Product> findByDescriptionContainingIgnoreCase(String description);
    Mono<Product> addProduct(Product product);
    Mono<Product> modifyProduct(String id, Product product) throws ProductNotFoundException;
    Mono<Product> removeProduct(String id) throws ProductNotFoundException;
}
