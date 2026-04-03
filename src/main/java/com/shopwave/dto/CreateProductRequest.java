// student ID: ATE/0561/14
package com.shopwave.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateProductRequest {
    @NotBlank(message = "Product name cannot be blank")
    private String name;
    
    private String description;
    
    @Positive(message = "Price must be positive")
    private BigDecimal price;
    
    @Min(value = 0, message = "Stock cannot be negative")
    private int stock;
    
    private Long categoryId;
}
