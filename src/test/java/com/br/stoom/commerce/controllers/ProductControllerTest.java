package com.br.stoom.commerce.controllers;

import com.br.stoom.commerce.dto.ProductDTO;
import com.br.stoom.commerce.repository.ProductRepository;
import com.br.stoom.commerce.service.DefaultProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private DefaultProductService defaultProductService;
    @MockitoBean
    private ProductRepository productRepository;
    @MockitoBean
    private ModelMapper modelMapper;
    @MockitoBean
    private ProductController productController;

    @Test
    void shouldReturnAllProducts() throws Exception {
        final ProductDTO product1 = createProductDTO("1", "Tênis Nike", "Calçados", "Nike", "Confortável", 299.99, 50, "IN_STOCK");
        final ProductDTO product2 = createProductDTO("2", "Blusa Adidas", "Vestuário", "Adidas", "Leve", 129.99, 20, "OUT_OF_STOCK");


        final List<ProductDTO> products = Arrays.asList(product1, product2);
        final Pageable pageable = PageRequest.of(0, 10);
        final Page<ProductDTO> page = new PageImpl<>(products, pageable, products.size());

        doReturn(page).when(defaultProductService).getAllProducts(pageable);

        mockMvc.perform(get("/products?page=0&size=10"))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                    {
                        "content": [
                            {
                                "id": "1",
                                "title": "Tênis Nike",
                                "category": "Calçados",
                                "brand": "Nike",
                                "description": "Confortável",
                                "price": 299.99,
                                "stock": 50,
                                "availability": "IN_STOCK"
                            },
                            {
                                "id": "2",
                                "title": "Blusa Adidas",
                                "category": "Vestuário",
                                "brand": "Adidas",
                                "description": "Leve",
                                "price": 129.99,
                                "stock": 20,
                                "availability": "OUT_OF_STOCK"
                            }
                        ],
                        "page": {
                            "size": 10,
                            "number": 0,
                            "totalElements": 2,
                            "totalPages": 1
                        }
                    }
            """));
    }

    @Test
    void testCreateProduct_Success() throws Exception {
        ProductDTO productDTO = createProductDTO("1", "Tênis Nike", "Calçados", "Nike", "Confortável", 299.99, 50, "IN_STOCK");

        doThrow(new RuntimeException("Unexpected error")).when(defaultProductService).creatingProduct(productDTO);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .post("/products/create")
                        .contentType("application/json")
                        .content(new ObjectMapper().writeValueAsString(productDTO)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().string("Created product successfully"))
                .andReturn();

        String responseContent = result.getResponse().getContentAsString();
        assertEquals("Created product successfully", responseContent);

        verify(defaultProductService).creatingProduct(any(ProductDTO.class));
    }

    @Test
    void testCreateProduct_Failure() throws Exception {

       final ProductDTO productDTO = createProductDTO("1", "Tênis Nike", "Calçados", "Nike", "Confortável", 299.99, 50, "IN_STOCK");

        doThrow(new RuntimeException("Error creating product")).when(defaultProductService).creatingProduct(any(ProductDTO.class));

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/products/create")
                        .contentType("application/json")
                        .content(new ObjectMapper().writeValueAsString(productDTO)))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("An unexpected error occurred while creating the product."));

        verify(defaultProductService, times(1)).creatingProduct(any(ProductDTO.class));
    }

    @Test
    void testGetProductsForStock_Success() throws Exception {

        final Pageable pageable = PageRequest.of(0, 10);
        final Page<ProductDTO> pageOfStockProducts = mock(Page.class);

        when(defaultProductService.getStockProducts(pageable)).thenReturn(pageOfStockProducts);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/products/stock")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").exists());

        verify(defaultProductService, times(1)).getStockProducts(pageable);
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
