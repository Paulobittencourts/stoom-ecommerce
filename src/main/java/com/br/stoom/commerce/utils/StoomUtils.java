package com.br.stoom.commerce.utils;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public class StoomUtils {

    public static Pageable createPageable(final int page, final int size) {
        return PageRequest.of(page, size);
    }
}
