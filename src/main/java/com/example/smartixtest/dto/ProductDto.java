package com.example.smartixtest.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDto {
    
    private Long id;
    
    @NotBlank(message = "Название товара не может быть пустым")
    private String title;
    
    @NotNull(message = "Цена товара должна быть указана")
    @Positive(message = "Цена товара должна быть положительной")
    private BigDecimal price;
    
    private String description;
    
    private String category;
    
    private String image;
    
    private RatingDto rating;
} 