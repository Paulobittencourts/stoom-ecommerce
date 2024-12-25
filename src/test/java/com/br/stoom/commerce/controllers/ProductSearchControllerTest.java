package com.br.stoom.commerce.controllers;

import com.br.stoom.commerce.dto.ProductDTO;
import com.br.stoom.commerce.service.DefaultProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductSearchController.class)
class ProductSearchControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private DefaultProductService defaultProductService;

    @Test
    void testGetProductByBrand_Success() throws Exception {
        String brand = "Puma";
        Pageable pageable = PageRequest.of(0, 10);
        Page<ProductDTO> mockPage = mock(Page.class);
        when(defaultProductService.findProductByBrand(brand, pageable)).thenReturn(mockPage);
        mockMvc.perform(get("/products/search/brands")
                        .param("brand", brand)
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk());
        verify(defaultProductService, times(1)).findProductByBrand(brand, pageable);
    }

    @Test
    void testGetProductByBrand_ExceptionHandling() throws Exception {
        String brand = "Puma";
        int page = 0;
        int size = 10;
        when(defaultProductService.findProductByBrand(eq(brand), any(Pageable.class)))
                .thenThrow(new RuntimeException("Unexpected error"));
        mockMvc.perform(get("/products/search/brands")
                        .param("brand", brand)
                        .param("page", String.valueOf(page))
                        .param("size", String.valueOf(size)))
                .andExpect(status().isInternalServerError()) 
                .andExpect(content().string(""));
    }

    @Test
    void testGetProductByCategory_Success() throws Exception {
        String category = "Calçados";
        int page = 0;
        int size = 10;
        ProductDTO product1 = createProductDTO("1", "Tênis Nike", "Calçados", "Nike", "Confortável", 299.99, 50, "IN_STOCK");
        ProductDTO product2 = createProductDTO("2", "Tênis Nike", "Calçados", "Adidas", "Leve", 129.99, 20, "IN_STOCK");
        Page<ProductDTO> productPage = new PageImpl<>(List.of(product1, product2));

        when(defaultProductService.findProductByCategory(eq(category), any(Pageable.class)))
                .thenReturn(productPage);

        mockMvc.perform(get("/products/search/category")
                        .param("category", category)
                        .param("page", String.valueOf(page))
                        .param("size", String.valueOf(size)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].title").value("Tênis Nike"))
                .andExpect(jsonPath("$.content[1].title").value("Tênis Nike"));
    }

    private ProductDTO createProductDTO(final String id, final String title, final String category,
                                        final String brand,final String description,
                                        final double price,
                                        final int stock,final String availability) {
        final ProductDTO productDTO = new ProductDTO();
        productDTO.setId(id);
        productDTO.setTitle(title);
        productDTO.setCategory(category);
        productDTO.setBrand(brand);
        productDTO.setDescription(description);
        productDTO.setPrice(price);
        productDTO.setStock(stock);
        productDTO.setAvailability(availability);
        return productDTO;
    }
}
