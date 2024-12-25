package com.br.stoom.commerce.controllers;


import com.br.stoom.commerce.dto.ProductDTO;
import com.br.stoom.commerce.dto.resquest.StockRequest;
import com.br.stoom.commerce.exceptions.ProductCreationException;
import com.br.stoom.commerce.exceptions.ProductNotFoundException;
import com.br.stoom.commerce.service.DefaultProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import static com.br.stoom.commerce.utils.StoomUtils.createPageable;


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
    public PagedModel<ProductDTO> getAllProducts(@RequestParam(required = false, defaultValue = "0") int page,
                                                 @RequestParam(required = false, defaultValue = "10") int size) {
        final Page<ProductDTO> pageOfProducts = defaultProductService.getAllProducts(createPageable(page, size));
        return new PagedModel<>(pageOfProducts);
    }

    @PostMapping("/create")
    public ResponseEntity<String> createProduct(@Validated @RequestBody ProductDTO productDTO) {
        try {
            defaultProductService.creatingProduct(productDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body("Created product successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred while creating the product.");
        }
    }

    @GetMapping("/stock")
    public PagedModel<ProductDTO> getProductsForStock(@RequestParam(required = false, defaultValue = "0") int page,
                                                      @RequestParam(required = false, defaultValue = "10") int size) {
        try {
            final Page<ProductDTO> pageStockOfProducts = defaultProductService.getStockProducts(createPageable(page, size));
            return new PagedModel<>(pageStockOfProducts);
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "An unexpected error occurred while fetching products.", e);
        }
    }

    @PostMapping("/update/stock")
    public ResponseEntity<String> updateProductAvailability(
            @Validated @RequestBody StockRequest stock) {
        try {
            defaultProductService.updateAvailability(stock.getStock(), stock.getProductID());
            return ResponseEntity.status(HttpStatus.OK)
                    .body("Product availability updated successfully.");
        } catch (ProductNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Product not found with ID: " + stock.getProductID());
        } catch (ProductCreationException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to update product availability: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred: " + e.getMessage());
        }
    }

    @DeleteMapping
    public ResponseEntity<String> deleteProductByID(@RequestParam Long productID) {
        try {
            final Boolean isDeleted = defaultProductService.deleteByID(productID);

            if (Boolean.TRUE.equals(isDeleted)) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Product successfully deleted");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Unexpected error occurred while deleting the product: " + e.getMessage());
        }
    }
}
