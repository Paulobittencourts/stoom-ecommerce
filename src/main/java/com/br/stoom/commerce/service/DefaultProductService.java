package com.br.stoom.commerce.service;

import com.br.stoom.commerce.dto.ProductDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DefaultProductService {

    Page<ProductDTO> getAllProducts(final Pageable pageable);

    void creatingProduct(final ProductDTO productDTO);

    Page<ProductDTO> getStockProducts(final Pageable pageable);

    void updateAvailability(final Integer stock, final Long productId);

    Page<ProductDTO> findProductByBrand(final String brand,final Pageable pageable);

    Page<ProductDTO> findProductByCategory(final String category,final Pageable pageable);

    Page<ProductDTO> findProductByBrandAndCategory(final String brand, final String category,final Pageable pageable);

    Boolean deleteByID(final Long productID);
}
