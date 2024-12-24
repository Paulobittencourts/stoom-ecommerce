package com.br.stoom.commerce.service.impl;

import com.br.stoom.commerce.dto.ProductDTO;
import com.br.stoom.commerce.repository.ProductRepository;
import com.br.stoom.commerce.service.DefaultProductService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements DefaultProductService {

    private final ProductRepository productRepository;

    private final ModelMapper modelMapper;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, ModelMapper modelMapper) {
        this.productRepository = productRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public Page<ProductDTO> getAllProducts(final Pageable pageable) {
        final List<ProductDTO> productDTOs = productRepository.findAll(pageable)
                .stream()
                .map(product ->
                        modelMapper.map(product, ProductDTO.class))
                .toList();

        return new PageImpl<>(productDTOs);
    }
}
