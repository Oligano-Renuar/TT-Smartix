package com.example.smartixtest.mapper;

import com.example.smartixtest.dto.ProductDto;
import com.example.smartixtest.dto.RatingDto;
import com.example.smartixtest.model.Category;
import com.example.smartixtest.model.Product;
import com.example.smartixtest.model.Rating;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public Product toEntity(ProductDto dto) {
        if (dto == null) {
            return null;
        }
        
        Rating rating = null;
        if (dto.getRating() != null) {
            rating = Rating.builder()
                    .rate(dto.getRating().getRate())
                    .count(dto.getRating().getCount())
                    .build();
        }
        
        Category category = null;
        if (dto.getCategory() != null) {
            category = Category.builder()
                    .name(dto.getCategory())
                    .build();
        }
        
        return Product.builder()
                .id(dto.getId())
                .title(dto.getTitle())
                .price(dto.getPrice())
                .description(dto.getDescription())
                .category(category)
                .image(dto.getImage())
                .rating(rating)
                .build();
    }
    
    public ProductDto toDto(Product entity) {
        if (entity == null) {
            return null;
        }
        
        RatingDto ratingDto = null;
        if (entity.getRating() != null) {
            ratingDto = RatingDto.builder()
                    .rate(entity.getRating().getRate())
                    .count(entity.getRating().getCount())
                    .build();
        }
        
        String categoryName = null;
        if (entity.getCategory() != null) {
            categoryName = entity.getCategory().getName();
        }
        
        return ProductDto.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .price(entity.getPrice())
                .description(entity.getDescription())
                .category(categoryName)
                .image(entity.getImage())
                .rating(ratingDto)
                .build();
    }
    
    public void updateEntityFromDto(ProductDto dto, Product entity) {
        if (dto == null || entity == null) {
            return;
        }
        
        if (dto.getTitle() != null) {
            entity.setTitle(dto.getTitle());
        }
        
        if (dto.getPrice() != null) {
            entity.setPrice(dto.getPrice());
        }
        
        if (dto.getDescription() != null) {
            entity.setDescription(dto.getDescription());
        }
        
        if (dto.getImage() != null) {
            entity.setImage(dto.getImage());
        }
        
        // Обновление рейтинга
        if (dto.getRating() != null) {
            if (entity.getRating() == null) {
                entity.setRating(new Rating());
            }
            
            if (dto.getRating().getRate() != null) {
                entity.getRating().setRate(dto.getRating().getRate());
            }
            
            if (dto.getRating().getCount() != null) {
                entity.getRating().setCount(dto.getRating().getCount());
            }
        }
        
        // Категорию обновляем отдельно через сервис, т.к. там нужен поиск или создание
    }
} 