package com.br.stoom.commerce.controllers;


import com.br.stoom.commerce.dto.ProductDTO;
import com.br.stoom.commerce.service.DefaultProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.br.stoom.commerce.utils.StoomUtils.createPageable;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products/search")
public class ProductSearchController {

    private DefaultProductService defaultProductService;

    @Autowired
    public ProductSearchController(DefaultProductService defaultProductService) {
        this.defaultProductService = defaultProductService;
    }

    @GetMapping("/brands")
    public PagedModel<ProductDTO> getProductByBrand(@RequestParam String brand,
                                                    @RequestParam(required = false, defaultValue = "0") int page,
                                                    @RequestParam(required = false, defaultValue = "10") int size) {
        final Page<ProductDTO> productDTOs =
                defaultProductService.findProductByBrand(brand, createPageable(page, size));
        return new PagedModel<>(productDTOs);
    }

    @GetMapping("/category")
    public PagedModel<ProductDTO> getProductByCategory(@RequestParam String category,
                                                       @RequestParam(required = false, defaultValue = "0") int page,
                                                       @RequestParam(required = false, defaultValue = "10") int size) {
        final Page<ProductDTO> productDTOS =
                defaultProductService.findProductByCategory(category, createPageable(page, size));
        return new PagedModel<>(productDTOS);
    }

    @GetMapping
    public PagedModel<ProductDTO> getProductBySearch(@RequestParam String brand,
                                                     @RequestParam String category,
                                                     @RequestParam(required = false, defaultValue = "0") int page,
                                                     @RequestParam(required = false, defaultValue = "10") int size) {
        final Page<ProductDTO> productByBrandAndCategory =
                defaultProductService.findProductByBrandAndCategory(brand, category, createPageable(page, size));
        return new PagedModel<>(productByBrandAndCategory);
    }
}
