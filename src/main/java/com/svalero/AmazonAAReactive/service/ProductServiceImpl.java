package com.svalero.AmazonAAReactive.service;

import com.svalero.AmazonAAReactive.domain.Product;
import com.svalero.AmazonAAReactive.exception.ProductNotFoundException;
import com.svalero.AmazonAAReactive.repository.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ProductServiceImpl implements ProductService{
    @Autowired
    private ProductRepository productRepository;
    @Override
    public Flux<Product> findAll() {
        return productRepository.findAll();
    }

    @Override
    public Mono<Product> findById(String id) throws ProductNotFoundException{
        return productRepository.findById(id).switchIfEmpty(Mono.defer(() -> Mono.error(new ProductNotFoundException())));
    }

    @Override
    public Flux<Product> findByNameContaining(String name) {
        return productRepository.findByNameContaining(name);
    }

    @Override
    public Flux<Product> findByCategory(String category) {
        return productRepository.findByCategory(category);
    }

    @Override
    public Flux<Product> findByDescriptionContainingIgnoreCase(String description) {
        return productRepository.findByDescriptionContainingIgnoreCase(description);
    }

    @Override
    public Mono<Product> addProduct(Product product) {
        return productRepository.save(product);
    }

    @Override
    public Mono<Product> modifyProduct(String id, Product product) throws ProductNotFoundException{
        Mono<Product> existingProduct = productRepository.findById(id).switchIfEmpty(Mono.defer(() -> Mono.error(new ProductNotFoundException())));
        Product newProduct = existingProduct.block();
        ModelMapper mapper = new ModelMapper();
        newProduct = mapper.map(product, Product.class);
        newProduct.setId(id);
        return productRepository.save(newProduct);
    }

    @Override
    public Mono<Product> removeProduct(String id) throws ProductNotFoundException{
        Mono<Product> existingProduct = productRepository.findById(id).switchIfEmpty(Mono.defer(() -> Mono.error(new ProductNotFoundException())));
        productRepository.deleteById(id).block();
        return existingProduct;
    }
}
