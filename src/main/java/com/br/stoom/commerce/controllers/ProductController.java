package com.br.stoom.commerce.controllers;


import com.br.stoom.commerce.dto.ProductDTO;
import com.br.stoom.commerce.service.DefaultProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;


@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {

    private DefaultProductService defaultProductService;

    @Autowired
    public ProductController(DefaultProductService defaultProductService) {
        this.defaultProductService = defaultProductService;
    }

    @GetMapping
    public PagedModel<ProductDTO> getAllProducts(@RequestParam int page,
                                                 @RequestParam int size){
        final Pageable pageable = PageRequest.of(page, size);
        final Page<ProductDTO> pageOfProducts = defaultProductService.getAllProducts(pageable);

        return new PagedModel<>(pageOfProducts);

    }

    @PostMapping("/create")
    public ResponseEntity<String> createProduct(@Validated @RequestBody ProductDTO productDTO){
        try {
            defaultProductService.creatingProduct(productDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body("Created product successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred while creating the product.");
        }

    }
}
