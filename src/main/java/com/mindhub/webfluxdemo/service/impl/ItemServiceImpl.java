package com.mindhub.webfluxdemo.service.impl;

import com.mindhub.webfluxdemo.handlers.NotFoundException;
import com.mindhub.webfluxdemo.models.Item;
import com.mindhub.webfluxdemo.repositories.ItemRepository;
import com.mindhub.webfluxdemo.service.ItemService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;

    public ItemServiceImpl(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @Override
    public Mono<Item> getItemById(Long id) {
        return itemRepository.findById(id)
                .switchIfEmpty(Mono.error(new NotFoundException("Item with ID " + id + " not found")));
    }

    @Override
    public Flux<Item> getAllItems() {
        return itemRepository.findAll();
    }

    @Override
    public Mono<Item> saveItem(Item item) {
        return itemRepository.save(item);
    }

    @Override
    public Mono<Item> updateItem(Long id, Item item) {
        return itemRepository.findById(id)
                .flatMap(existingItem -> {
                    existingItem.setName(item.getName());
                    return itemRepository.save(existingItem);
                })
                .switchIfEmpty(Mono.error(new NotFoundException("Item with ID " + id + " not found")));
    }

    @Override
    public Mono<Void> deleteItem(Long id) {
        return itemRepository.existsById(id)
                .flatMap(exists -> {
                    if (exists) {
                        return itemRepository.deleteById(id);
                    } else {
                        return Mono.error(new NotFoundException("Item with ID " + id + " not found"));
                    }
                });
    }
}