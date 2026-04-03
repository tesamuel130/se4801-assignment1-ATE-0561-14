package com.shopwave.service;

import com.shopwave.dto.CreateProductRequest;
import com.shopwave.dto.ProductDTO;
import com.shopwave.mapper.ProductMapper;
import com.shopwave.model.Category;
import com.shopwave.model.Product;
import com.shopwave.repository.CategoryRepository;
import com.shopwave.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private ProductService productService;

    @Test
    void createProduct_Success() {
        // Arrange
        CreateProductRequest request = new CreateProductRequest();
        request.setName("Gaming Mouse");
        request.setPrice(BigDecimal.valueOf(50));
        request.setStock(10);
        request.setCategoryId(1L);

        Category category = new Category();
        category.setId(1L);
        category.setName("Electronics");

        Product mappedProduct = new Product();
        mappedProduct.setName("Gaming Mouse");
        mappedProduct.setPrice(BigDecimal.valueOf(50));
        mappedProduct.setStock(10);
        mappedProduct.setCategory(category);

        Product savedProduct = new Product();
        savedProduct.setId(100L);
        savedProduct.setName("Gaming Mouse");
        savedProduct.setPrice(BigDecimal.valueOf(50));
        savedProduct.setStock(10);
        savedProduct.setCategory(category);

        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(100L);
        productDTO.setName("Gaming Mouse");

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(productMapper.toEntity(request, category)).thenReturn(mappedProduct);
        when(productRepository.save(mappedProduct)).thenReturn(savedProduct);
        when(productMapper.toDTO(savedProduct)).thenReturn(productDTO);

        // Act
        ProductDTO result = productService.createProduct(request);

        // Assert
        assertNotNull(result);
        assertEquals(100L, result.getId());
        assertEquals("Gaming Mouse", result.getName());
        verify(productRepository).save(any(Product.class));
    }

    @Test
    void createProduct_CategoryNotFound_ThrowsException() {
        // Arrange
        CreateProductRequest request = new CreateProductRequest();
        request.setCategoryId(999L);

        when(categoryRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
                () -> productService.createProduct(request));
        
        assertEquals("Category not found with id: 999", exception.getMessage());
        verify(productRepository, never()).save(any(Product.class));
    }
}
