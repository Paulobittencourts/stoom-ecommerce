package com.br.stoom.commerce.service.impl;

import com.br.stoom.commerce.dto.ProductDTO;
import com.br.stoom.commerce.exceptions.ProductCreationException;
import com.br.stoom.commerce.model.ProductModel;
import com.br.stoom.commerce.repository.ProductRepository;
import com.br.stoom.commerce.service.DefaultProductService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, ModelMapper modelMapper) {
        this.productRepository = productRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public Page<ProductDTO> getAllProducts(final Pageable pageable) {

        final Page<ProductModel> pageOfProducts = productRepository.findAll(pageable);

        final List<ProductDTO> productDTOs = pageOfProducts.getContent().stream()
                .map(product -> modelMapper.map(product, ProductDTO.class))
                .toList();

        return new PageImpl<>(productDTOs, pageable, pageOfProducts.getTotalElements());

    }

    @Override
    public void creatingProduct(final ProductDTO productDTO) {
        try{
            logger.info("Creating product: {}", productDTO.getTitle());
            final ProductModel productModel = modelMapper.map(productDTO, ProductModel.class);
            productRepository.save(productModel);
        }catch (Exception e){
            throw new ProductCreationException("Failed to save product: " + e.getMessage(), e);
        }
    }
}
