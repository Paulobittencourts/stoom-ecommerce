package com.br.stoom.commerce.exceptions;

public class ProductCreationException extends RuntimeException {

    public ProductCreationException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public ProductCreationException(final String message) {
        super(message);
    }
}
