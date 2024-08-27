package com.mindhub.webfluxdemo.service;

import com.mindhub.webfluxdemo.models.Item;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ItemService {
    Mono<Item> getItemById(Long id);
    Flux<Item> getAllItems();
    Mono<Item> saveItem(Item item);
    Mono<Item> updateItem(Long id, Item item);
    Mono<Void> deleteItem(Long id);
}