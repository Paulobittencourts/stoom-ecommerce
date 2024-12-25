package com.br.stoom.commerce.service.impl;

import com.br.stoom.commerce.dto.ProductDTO;
import com.br.stoom.commerce.exceptions.ProductCreationException;
import com.br.stoom.commerce.exceptions.ProductNotFoundException;
import com.br.stoom.commerce.model.Enum.AvailabilityStatus;
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
import java.util.Optional;

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
        try {
            logger.info("Creating product: {}", productDTO.getTitle());
            final ProductModel productModel = modelMapper.map(productDTO, ProductModel.class);
            productRepository.save(productModel);
        } catch (Exception e) {
            throw new ProductCreationException("Failed to save product: " + e.getMessage(), e);
        }
    }

    @Override
    public Page<ProductDTO> getStockProducts(final Pageable pageable) {
        final Page<ProductModel> pageOfProducts = productRepository.findByAvailability(AvailabilityStatus.IN_STOCK, pageable);

        final List<ProductDTO> productDTOs = pageOfProducts.getContent().stream()
                .map(product -> modelMapper.map(product, ProductDTO.class))
                .toList();

        return new PageImpl<>(productDTOs, pageable, pageOfProducts.getTotalElements());
    }

    @Override
    public void updateAvailability(final Integer stock, final Long productId) {
        try {

            final ProductModel product = productRepository.findById(productId)
                    .orElseThrow(() -> new ProductCreationException(
                            "Product with ID " + productId + " not found"));

            logger.info("Updating product: {}", product.getTitle());

            final AvailabilityStatus availability = (stock != null && stock > 0)
                    ? AvailabilityStatus.IN_STOCK
                    : AvailabilityStatus.OUT_OF_STOCK;
            product.setAvailability(availability);
            product.setStock(stock);

            logger.info("Availability updated for product: {} to {}", product.getTitle(), availability);
            productRepository.save(product);

        } catch (ProductNotFoundException e) {
            logger.error("Product not found: {}", e.getMessage());
            throw new ProductNotFoundException(e.getMessage(), e);
        } catch (Exception e) {
            logger.error("Failed to update product availability: {}", e.getMessage(), e);
            throw new ProductCreationException("Failed to update product: " + e.getMessage(), e);
        }
    }

    @Override
    public Page<ProductDTO> findProductByBrand(final String brand, final Pageable pageable) {
        try {
            final Page<ProductModel> pageOfProducts = productRepository.findByBrand(brand, pageable);

            final List<ProductDTO> productDTOs = pageOfProducts.getContent().stream()
                    .filter(product -> AvailabilityStatus.IN_STOCK.equals(product.getAvailability()))
                    .map(product -> modelMapper.map(product, ProductDTO.class))
                    .toList();

            return new PageImpl<>(productDTOs, pageable, pageOfProducts.getTotalElements());
        } catch (ProductNotFoundException e) {
            logger.error("No products found for brand: {}", brand, e);
            return Page.empty();
        } catch (Exception e) {
            logger.error("Error occurred while fetching products by brand: {}", brand, e);
            return Page.empty();
        }
    }

    @Override
    public Page<ProductDTO> findProductByCategory(final String category, final Pageable pageable) {
        try {
            final Page<ProductModel> pageOfProducts = productRepository.findByCategory(category, pageable);

            final List<ProductDTO> productDTOS = filterAndMapperProductStock(pageOfProducts);

            return new PageImpl<>(productDTOS, pageable, pageOfProducts.getTotalElements());
        } catch (Exception e) {
            logger.error("Error occurred while fetching products by category: {}", category, e);
            return Page.empty();
        }
    }

    @Override
    public Page<ProductDTO> findProductByBrandAndCategory(final String brand,
                                                          final String category,
                                                          final Pageable pageable) {
        try {
            final Page<ProductModel> byBrandAndCategory = productRepository.findByBrandAndCategory(brand, category, pageable);
            final List<ProductDTO> productDTOS = filterAndMapperProductStock(byBrandAndCategory);
            return new PageImpl<>(productDTOS, pageable, byBrandAndCategory.getTotalElements());
        } catch (Exception e) {
            logger.error("Error occurred while fetching products by brand and category: {}", brand, e);
            return Page.empty();
        }
    }

    @Override
    public Boolean deleteByID(final Long productID) {
        final Optional<ProductModel> product = productRepository.findById(productID);
        if (product.isPresent()) {
            productRepository.deleteById(productID);
            return true;
        }
        return false;
    }

    private List<ProductDTO> filterAndMapperProductStock(final Page<ProductModel> pageOfProducts) {
        return pageOfProducts.getContent().stream()
                .filter(product -> AvailabilityStatus.IN_STOCK.equals(product.getAvailability()))
                .map(product -> modelMapper.map(product, ProductDTO.class))
                .toList();
    }
}
