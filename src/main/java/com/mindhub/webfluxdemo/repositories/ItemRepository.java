package com.mindhub.webfluxdemo.repositories;
import com.mindhub.webfluxdemo.models.Item;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface ItemRepository extends ReactiveCrudRepository<Item, Long> {
}