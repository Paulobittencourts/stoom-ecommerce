package com.br.stoom.commerce.controllers;


import com.br.stoom.commerce.dto.ProductDTO;
import com.br.stoom.commerce.model.ProductModel;
import com.br.stoom.commerce.repository.ProductRepository;
import com.br.stoom.commerce.service.DefaultProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

;import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {

    private DefaultProductService defaultProductService;

    private ProductRepository productRepository;

    @Autowired
    public ProductController(DefaultProductService defaultProductService, ProductRepository productRepository) {
        this.defaultProductService = defaultProductService;
        this.productRepository = productRepository;
    }

    @GetMapping
    public Page<ProductDTO> getAllProducts(@PageableDefault(size = 5) Pageable pageable){
        return defaultProductService.getAllProducts(pageable);
    }
}
