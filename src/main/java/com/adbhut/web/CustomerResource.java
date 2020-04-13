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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import com.adbhut.web.model.Customer;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(value = "/customers", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class CustomerResource {

    private Map<Integer, Customer> map = new HashMap<>();
    private List<Customer> customers = new ArrayList<>();

    public CustomerResource() {
        Customer customer = new Customer(1, "John Doe", ZonedDateTime.now()
                .with(LocalTime.MAX)
                .minus(1, ChronoUnit.HOURS));
        customers.add(customer);
        map.put(1, customer);

        customer = new Customer(2, "John Flaming", ZonedDateTime.now()
                .with(LocalTime.MAX)
                .minus(2, ChronoUnit.HOURS));
        customers.add(customer);
        map.put(2, customer);

        customer = new Customer(3, "John Gavin", ZonedDateTime.now()
                .with(LocalTime.MAX)
                .minus(3, ChronoUnit.HOURS));
        customers.add(customer);
        map.put(3, customer);

    }

    @GetMapping()
    public ResponseEntity<List<Customer>> getCustomers() {
        log.info("get list of products");
        return ResponseEntity.ok()
                .body(customers);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Customer> getCustomer(@PathVariable int id, WebRequest webRequest) {
        log.info("Product detail for id [{}]", id);

        Customer customer = map.get(id);
        ZonedDateTime lastModifiedDate = customer.getLastModified();
        if (webRequest.checkNotModified(lastModifiedDate.toEpochSecond())) {
            return null;
        }
        CacheControl cacheControl = CacheControl.maxAge(30, TimeUnit.MINUTES)
                .cachePublic();

        return ResponseEntity.ok()
                .cacheControl(cacheControl)
                .lastModified(customer.getLastModified())
                .body(customer);
    }
}
