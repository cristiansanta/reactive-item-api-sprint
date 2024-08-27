package com.mindhub.webfluxdemo.controller;

import com.mindhub.webfluxdemo.handlers.NotFoundException;
import com.mindhub.webfluxdemo.models.Item;
import com.mindhub.webfluxdemo.service.ItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/items")
@Tag(name = "Item", description = "Item management APIs")
public class ItemController {

    private static final Logger logger = LoggerFactory.getLogger(ItemController.class);

    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get an item by its id", responses = {
            @ApiResponse(responseCode = "200", description = "Item found",
                    content = @Content(schema = @Schema(implementation = Item.class))),
            @ApiResponse(responseCode = "404", description = "Item not found")
    })
    public Mono<ResponseEntity<Item>> getItemById(@Parameter(description = "id of the item to be searched")
                                                  @PathVariable Long id) {
        logger.info("Fetching item with id: {}", id);
        return itemService.getItemById(id)
                .map(item -> {
                    logger.info("Found item: {}", item);
                    return ResponseEntity.ok(item);
                })
                .defaultIfEmpty(ResponseEntity.notFound().build())
                .doOnError(error -> logger.error("Error fetching item with id: {}", id, error));
    }

    @GetMapping
    @Operation(summary = "Get all items")
    public Flux<Item> getAllItems() {
        logger.info("Fetching all items");
        return itemService.getAllItems()
                .doOnComplete(() -> logger.info("Finished fetching all items"))
                .doOnError(error -> logger.error("Error fetching all items", error));
    }

    @PostMapping
    @Operation(summary = "Create a new item")
    public Mono<Item> createItem(@RequestBody Item item) {
        logger.info("Creating new item: {}", item);
        return itemService.saveItem(item)
                .doOnSuccess(savedItem -> logger.info("Created item: {}", savedItem))
                .doOnError(error -> logger.error("Error creating item: {}", item, error));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing item")
    public Mono<ResponseEntity<Item>> updateItem(@PathVariable Long id, @RequestBody Item item) {
        logger.info("Updating item with id: {}", id);
        return itemService.updateItem(id, item)
                .map(updatedItem -> {
                    logger.info("Updated item: {}", updatedItem);
                    return ResponseEntity.ok(updatedItem);
                })
                .defaultIfEmpty(ResponseEntity.notFound().build())
                .doOnError(error -> logger.error("Error updating item with id: {}", id, error));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an item")
    @ApiResponse(responseCode = "204", description = "Item successfully deleted")
    @ApiResponse(responseCode = "404", description = "Item not found")
    public Mono<ResponseEntity<Void>> deleteItem(@PathVariable Long id) {
        return itemService.deleteItem(id)
                .then(Mono.just(ResponseEntity.noContent().<Void>build()))
                .onErrorResume(e -> Mono.just(ResponseEntity.notFound().build()));
    }
}