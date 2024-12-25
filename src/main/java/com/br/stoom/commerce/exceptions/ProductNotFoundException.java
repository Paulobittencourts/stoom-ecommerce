package com.br.stoom.commerce.exceptions;

public class ProductNotFoundException extends RuntimeException{

    public ProductNotFoundException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public ProductNotFoundException(String message) {
        super(message);
    }

}
