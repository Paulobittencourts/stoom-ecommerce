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

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private DefaultProductService defaultProductService;

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
