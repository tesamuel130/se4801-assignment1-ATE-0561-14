package com.shopwave.repository;

import com.shopwave.model.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Test
    void findByNameContainingIgnoreCase_ReturnsProducts() {
        // Arrange
        Product p1 = new Product();
        p1.setName("Gaming Laptop");
        p1.setPrice(BigDecimal.valueOf(1500));
        p1.setStock(10);
        productRepository.save(p1);

        Product p2 = new Product();
        p2.setName("Office Mouse");
        p2.setPrice(BigDecimal.valueOf(50));
        p2.setStock(50);
        productRepository.save(p2);

        // Act
        List<Product> results = productRepository.findByNameContainingIgnoreCase("gAMing");

        // Assert
        assertFalse(results.isEmpty());
        assertEquals(1, results.size());
        assertEquals("Gaming Laptop", results.get(0).getName());

        // Act 2 (No match)
        List<Product> emptyResults = productRepository.findByNameContainingIgnoreCase("keyboard");

        // Assert 2
        assertTrue(emptyResults.isEmpty());
    }
}
