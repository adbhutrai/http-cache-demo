package com.adbhut.web;

import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import com.adbhut.web.model.Product;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(value = "/products", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class ProductResource {

    private Map<Integer, Product> map = new HashMap<>();
    private List<Product> products = new ArrayList<>();

    public ProductResource() {
        Product product = new Product(1, "Book", ZonedDateTime.now()
                .with(LocalTime.MAX)
                .minus(1, ChronoUnit.HOURS));
        products.add(product);
        map.put(1, product);

        product = new Product(2, "Tissue", ZonedDateTime.now()
                .with(LocalTime.MAX)
                .minus(2, ChronoUnit.HOURS));
        products.add(product);
        map.put(2, product);

        product = new Product(3, "Sanitizer", ZonedDateTime.now()
                .with(LocalTime.MAX)
                .minus(3, ChronoUnit.HOURS));
        products.add(product);
        map.put(3, product);

    }

    @GetMapping()
    public ResponseEntity<List<Product>> getProducts() {
        log.info("get list of products");
        return ResponseEntity.ok()
                .body(products);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable int id, WebRequest request) {
        log.info("Product detail for id [{}]", id);
//http://dolszewski.com/spring/http-cache-with-spring-examples/
        //https://dzone.com/articles/caching-with-apache-http-client-and-spring-resttem
        Product product = map.get(id);

        String modificationDate = product.getLastModified()
                .toString();
        String eTag = DigestUtils.md5DigestAsHex(modificationDate.getBytes());

        if (request.checkNotModified(eTag)) {
            return null;
        }
        CacheControl cacheControl = CacheControl.maxAge(30, TimeUnit.MINUTES)
                .cachePublic();
        return ResponseEntity.ok()
                .cacheControl(cacheControl)
                .body(product);
    }

}
