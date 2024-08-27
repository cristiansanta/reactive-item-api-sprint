package com.mindhub.webfluxdemo.handlers;

public class NotFoundException extends RuntimeException {
    public NotFoundException(Long id) {
        super("Item with ID " + id + " not found");
    }
}