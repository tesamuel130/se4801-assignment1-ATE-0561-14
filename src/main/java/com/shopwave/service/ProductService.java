// student ID: ATE/0561/14
package com.shopwave.service;

import com.shopwave.dto.CreateProductRequest;
import com.shopwave.dto.ProductDTO;
import com.shopwave.exception.ProductNotFoundException;
import com.shopwave.mapper.ProductMapper;
import com.shopwave.model.Category;
import com.shopwave.model.Product;
import com.shopwave.repository.CategoryRepository;
import com.shopwave.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper productMapper;

    public ProductDTO createProduct(CreateProductRequest request) {
        Category category = null;
        if (request.getCategoryId() != null) {
            category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new IllegalArgumentException("Category not found with id: " + request.getCategoryId()));
        }

        Product product = productMapper.toEntity(request, category);
        Product savedProduct = productRepository.save(product);
        return productMapper.toDTO(savedProduct);
    }

    @Transactional(readOnly = true)
    public Page<ProductDTO> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable)
                .map(productMapper::toDTO);
    }

    @Transactional(readOnly = true)
    public ProductDTO getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + id));
        return productMapper.toDTO(product);
    }

    @Transactional(readOnly = true)
    public List<ProductDTO> searchProducts(String name) {
        return productRepository.findByNameContainingIgnoreCase(name).stream()
                .map(productMapper::toDTO)
                .toList();
    }

    public ProductDTO updateStock(Long id, int delta) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + id));

        int newStock = product.getStock() + delta;
        if (newStock < 0) {
            throw new IllegalArgumentException("Stock cannot be negative");
        }

        product.setStock(newStock);
        Product updatedProduct = productRepository.save(product);

        return productMapper.toDTO(updatedProduct);
    }
}
