package com.br.stoom.commerce.controllers;


import com.br.stoom.commerce.dto.ProductDTO;
import com.br.stoom.commerce.service.DefaultProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


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
}
