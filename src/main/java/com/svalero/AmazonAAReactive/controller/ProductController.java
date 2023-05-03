package com.svalero.AmazonAAReactive.controller;

import com.svalero.AmazonAAReactive.domain.Product;
import com.svalero.AmazonAAReactive.exception.ErrorException;
import com.svalero.AmazonAAReactive.exception.ProductNotFoundException;
import com.svalero.AmazonAAReactive.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;
import java.util.Map;

@RestController
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping(value = "/products", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Product> getProducts(@RequestParam Map<String,String> data){
        if(data.containsKey("name")){
            return productService.findByNameContaining(data.get("name")).delayElements(Duration.ofSeconds(3));
        } else if(data.containsKey("category")){
            return productService.findByCategory(data.get("category")).delayElements(Duration.ofSeconds(3));
        } else if(data.containsKey("description")){
            return productService.findByDescriptionContainingIgnoreCase(data.get("description")).delayElements(Duration.ofSeconds(3));
        }
        return productService.findAll().delayElements(Duration.ofSeconds(3));
    }

    @GetMapping(value = "/products/{id}")
    public Mono<Product> getProduct(@PathVariable String id) throws ProductNotFoundException {
        Mono<Product> productMono = productService.findById(id);
        return productMono.delayElement(Duration.ZERO);
    }

    @PostMapping(value = "/products")
    public void addProduct(@RequestBody Product product){
        productService.addProduct(product).block();
    }

    @PutMapping("/products/{id}")
    public Mono<Product> modifyProduct(@PathVariable String id, @RequestBody Product product) throws ProductNotFoundException {
        Mono<Product> productMono = productService.modifyProduct(id, product);
        return ResponseEntity.status(HttpStatus.OK).body(productMono).getBody();
    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable String id) throws ProductNotFoundException{
        Mono<Product> productMono = productService.removeProduct(id);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ErrorException> handleProductNotFoundException(ProductNotFoundException pnfe){
        ErrorException errorException= new ErrorException(404, pnfe.getMessage());
        return new ResponseEntity<>(errorException, HttpStatus.NOT_FOUND);
    }
}
