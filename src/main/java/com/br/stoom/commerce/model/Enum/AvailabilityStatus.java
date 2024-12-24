package com.br.stoom.commerce.model.Enum;

public enum AvailabilityStatus {

    IN_STOCK("in_stock"),
    OUT_OF_STOCK("out_of_stock");

    private final String value;

    AvailabilityStatus(final String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
